package model;

import javafx.scene.image.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**The photo class
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class Photo implements Serializable {
    /**
     * The address of the photo
     */
    String address;
    /**
     * The caption of a photo
     */
    String caption;
    /**
     * The last modification date of a photo
     */
    Date date;
    /**
     * A list of tag
     */
    ArrayList<Tag> tagList;

    /**
     * Throws when trying add an existed tag to a photo
     */
    public static class RepeatedTagException extends RuntimeException {
        public RepeatedTagException(String s) {
            super(s);
        }
    }

    /**
     * Throws when try to add an unique value tag to a multiple value tag
     */
    public static class TagTypeUnmatchedException extends RuntimeException{
        public TagTypeUnmatchedException(String s) {
            super(s);
        }
    }

    /**
     * Throws when cannot get the Image instance from Photo
     */
    public static class ImageErrorException extends RuntimeException{
        public ImageErrorException(String s) {
            super(s);
        }
    }

    /**
     * Constructor of a photo instance
     * @param address file address of a photo
     * @throws FileNotFoundException throws when the file cannot be load or does not exist.
     */
    public Photo(String address) throws FileNotFoundException {
        File file = new File(address);
        if(file.exists()){
            this.address = file.getAbsolutePath();
            this.caption = "";
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(file.lastModified());
            this.date = cal.getTime();
            this.tagList = new ArrayList<>();
        }
        else{
            throw new FileNotFoundException("The file \"" + address + "\" doesn't exist.");
        }
    }

    /**
     * Get the javafx Image instance by address
     * @return an Image instance
     * @throws FileNotFoundException Throws when there are issues with the address
     */
    public Image getImage() throws FileNotFoundException {
        File file = new File(this.address);
        Image image;
        if(file.exists()){
            if(!file.isFile()){
                throw new FileNotFoundException(file.getAbsolutePath() + " is not an image file.");
            }
            else{
                try{
                    image = new Image(file.toURI().toString());
                    return image;
                }
                catch(Exception e){
                    throw new ImageErrorException("Error when loading " + file.getAbsolutePath() + " as an image");
                }
            }
        }
        else{
            throw new FileNotFoundException(file.getAbsolutePath() + " does not exist.");
        }
    }

    /**
     * Get the file address for a photo instance
     * @return file address of a photo
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * Get the caption of a photo
     * @return caption of a photo
     */
    public String getCaption(){
        return this.caption;
    }

    /**
     * Set the caption of the photo
     * @param caption input caption
     */
    public void setCaption(String caption){
        this.caption = caption;
    }

    /**
     * Get the date in a default date format
     * @return Date in yyyy-MM-dd HH:mm:ss
     */
    public String getDate(){
        return getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Get the date in a specific date format
     * @param dateFormat a specific date format
     * @return String value of a date
     */
    public String getDate(DateFormat dateFormat){
        return dateFormat.format(this.date.getTime());
    }

    /**
     * Getter method to get all the tags in a photo
     * @return a list of tags
     */
    public ArrayList<Tag> getTagList(){
        return this.tagList;
    }

    /**
     * Add an unique value tag by the combination of tagName and tagValue
     * @param tagName input tagName
     * @param tagValue input tagValue
     */
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

    /**
     * Add an unique value tag
     * @param tag input tag
     */
    public void addUniqueValueTag(UniqueValueTag tag){
        addUniqueValueTag(tag.tagName, tag.tagValue);
    }

    /**
     * Add a multiple value tag by a combination of tagName and tagValue
     * @param tagName input tagName
     * @param tagValue input tagValue
     */
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

    /**
     * Add a multiple value tag
     * @param tag input tag
     */
    public void addMultipleValueTag(MultipleValueTag tag){
        addMultipleValueTag(tag.tagName, tag.tagValues.get(0));
    }

    /**
     * Delete a unique value tag by its tagName
     * @param tagName input tagName
     */
    public void deleteUniqueValueTag(String tagName){
        for(Tag tag : this.tagList){
            if(tag.tagName.equals(tagName)){
                this.tagList.remove(tag);
                break;
            }
        }
    }

    /**
     * Delete a unique value tag
     * @param tag input tag
     */
    public void deleteUniqueValueTag(UniqueValueTag tag){
        this.tagList.remove(tag);
    }

    /**
     * Delete a multiple value tag by a combination of tagName and tagValue
     * @param tagName input tagName
     * @param tagValue input tagValue
     */
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

    /**
     * Delete a multiple value tag
     * @param tag tag that wants to delete
     */
    public void deleteMultipleValueTag(MultipleValueTag tag){
        deleteMultipleValueTag(tag.tagName, tag.tagValues.get(0));
    }

    /**
     * Check if the date of the photo is in this date range
     * @param earliestDate Date of earliest time
     * @param latestDate Date of latest time
     * @return true if the date of the photo is within the date range
     */
    public boolean inDateRange(Date earliestDate, Date latestDate){
        return this.date.getTime() >= earliestDate.getTime() && this.date.getTime() <= latestDate.getTime();
    }

    /**
     * Check if the photo has a specific tag
     * @param targetTag a certain tag
     * @return true if a photo has the input tag
     */
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

    /**
     * Set the default print value of a photo to its address
     * @return the address of the photo
     */
    @Override
    public String toString() {
        return this.address;
    }

    /**
     * equals method used to check if two photo have same address
     * @param o another photo
     * @return true if they have the same address
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(address, photo.address);
    }

    /**
     * hash method
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(address, caption, date, tagList);
    }
}