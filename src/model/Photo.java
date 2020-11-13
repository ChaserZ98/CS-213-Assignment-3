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

    private static class TagTypeUnmatchedException extends RuntimeException{
        public TagTypeUnmatchedException(String s) {
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

    public void addUniqueTag(String tagName, String tagValue){
        UniqueValueTag newTag = new UniqueValueTag(tagName, tagValue);

        int existedTagIndex = this.tagList.indexOf(newTag);
        if(this.tagList.contains(newTag)){
            Tag existedTag = this.tagList.get(existedTagIndex);
            if(existedTag instanceof MultipleValueTag){
                MultipleValueTag multipleValueTag = (MultipleValueTag) existedTag;
                throw new TagTypeUnmatchedException("The tag " + multipleValueTag.tagName + " already exists as an MultipleValueTag while the system tries to add the tag (" + tagName + "," + tagValue + ") as an UniqueValueTag.");
            }
            else{
                throw new RepeatedTagException("The tag (" + tagName + "," + tagValue + ") already exists.");
            }
        }
        else{
            this.tagList.add(newTag);
        }
    }
    public void addMultipleTag(String tagName, String tagValue){
        MultipleValueTag newTag = new MultipleValueTag(tagName);
        newTag.addTagValue(tagValue);

        int existedTagIndex = this.tagList.indexOf(newTag);
        if(this.tagList.contains(newTag)){
            Tag existedTag = this.tagList.get(existedTagIndex);
            if(existedTag instanceof UniqueValueTag){
                UniqueValueTag uniqueValueTag = (UniqueValueTag) existedTag;
                throw new TagTypeUnmatchedException("The tag (" + uniqueValueTag.tagName + "," + uniqueValueTag.tagValue + ") already exists as an UniqueValueTag while the system tries to add the tag (" + tagName + "," + tagValue + ") as an MultipleValueTag.");
            }
            else{
                throw new RepeatedTagException("The tag (" + tagName + "," + tagValue + ") already exists.");
            }
        }
        else{
            if(existedTagIndex >= 0){
                MultipleValueTag existedTag = (MultipleValueTag) this.tagList.get(existedTagIndex);
                existedTag.addTagValue(tagValue);
            }
            else{
                this.tagList.add(newTag);
            }
        }
    }
}