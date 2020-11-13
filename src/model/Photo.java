package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Photo{
    String address;
    Date date;
    ArrayList<Tag> tagList;

    private static class RepeatedTagException extends RuntimeException {
        public RepeatedTagException(String s) {
            super(s);
        }
    }

    public Photo(String address) throws FileNotFoundException {
        File file = new File(address);
        if(file.exists()){
            this.address = file.getAbsolutePath();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(file.lastModified());
            this.date = cal.getTime();
            this.tagList = new ArrayList<>();
//            System.out.println(file.getAbsolutePath());
        }
        else{
            throw new FileNotFoundException("The file \"" + address + "\" doesn't exist.");
        }
    }

    public void addTag(String tagName, String tagValue){
        Tag newTag = new Tag(tagName, tagValue);
        if(this.tagList.contains(newTag)){
            throw new RepeatedTagException("The tag (" + tagName + "," + tagValue + ") already exists.");
        }
        else{
            tagList.add(newTag);
        }
    }
}