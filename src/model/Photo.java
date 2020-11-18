package model;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Photo implements Serializable {
    String address;
    String caption;
    Date date;
    ArrayList<Tag> tagList;

    //sara add
    transient Image image;
    public Image getImage() {
        return image;
    }   //sara add


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
            this.caption = "";
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

    public String getCaption(){
        return this.caption;
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    public String getDate(){
        return getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public String getDate(DateFormat dateFormat){
        return dateFormat.format(this.date.getTime());
    }

    public void addUniqueValueTag(String tagName, String tagValue){
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

    public void addUniqueValueTag(UniqueValueTag tag){
        addUniqueValueTag(tag.tagName, tag.tagValue);
    }

    public void addMultipleValueTag(String tagName, String tagValue){
        MultipleValueTag newTag = new MultipleValueTag(tagName, tagValue);

        int existedTagIndex = -1;
        for(int i = 0; i < this.tagList.size(); i++){
            if(this.tagList.get(i).tagName.equals(tagName)){
                existedTagIndex = i;
                break;
            }
        }
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

    public void addMultipleValueTag(MultipleValueTag tag){
        addMultipleValueTag(tag.tagName, tag.tagValues.get(0));
    }

    public void deleteUniqueValueTag(String tagName){
        for(Tag tag : this.tagList){
            if(tag.tagName.equals(tagName)){
                this.tagList.remove(tag);
                break;
            }
        }
    }

    public void deleteUniqueValueTag(UniqueValueTag tag){
        this.tagList.remove(tag);
    }

    public void deleteMultipleValueTag(String tagName, String tagValue){
        for(Tag tag : this.tagList){
            if(tag.tagName.equals(tagName)){
                MultipleValueTag multipleValueTag = (MultipleValueTag) tag;
                int tagValueIndex = multipleValueTag.tagValues.indexOf(tagValue);
                if(tagValueIndex >= 0){
                    multipleValueTag.tagValues.remove(tagValueIndex);
                    if(multipleValueTag.tagValues.size() == 0){
                        this.tagList.remove(multipleValueTag);
                    }
                }
            }
        }
    }

    public void deleteMultipleValueTag(MultipleValueTag tag){
        deleteMultipleValueTag(tag.tagName, tag.tagValues.get(0));
    }

    public boolean inDateRange(Date earliestDate, Date latestDate){
        return this.date.getTime() >= earliestDate.getTime() && this.date.getTime() <= latestDate.getTime();
    }

    public boolean hasTag(Tag targetTag){
        if(targetTag == null){
            return false;
        }
        for(Tag tag : this.tagList){
            if(tag.tagName.equals(targetTag.tagName)){
                if(tag instanceof UniqueValueTag && tag.getClass() == targetTag.getClass()){
                    return ((UniqueValueTag) tag).tagValue.equals(((UniqueValueTag) targetTag).tagValue);
                }
                else if(tag instanceof MultipleValueTag && tag.getClass() == targetTag.getClass()){
                    return ((MultipleValueTag) tag).tagValues.contains(((MultipleValueTag) targetTag).tagValues.get(0));
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(address, photo.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, caption, date, tagList);
    }
}