package model;

import java.util.ArrayList;
import java.util.Objects;

/**The MultipleValueTag which is a subclass of Tag
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class MultipleValueTag extends Tag{
    /**
     * The first tag value
     */
    String tagValue;
    /**
     * List of tag values with the same tag Name
     */
    ArrayList<String> tagValues;

    /**
     * Throws when trying to insert a tag value that is already in the list
     */
    private static class RepeatedTagValueException extends RuntimeException{
        public RepeatedTagValueException(String s){
            super(s);
        }
    }

    /**
     * Multiple value tag constructor with one tag value
     * @param tagName the tag name of a tag
     * @param tagValue the tag value of a tag
     */
    public MultipleValueTag(String tagName, String tagValue){
        this.tagName = tagName;
        tagValues = new ArrayList<>();
        if(tagValue.length() != 0){
            tagValues.add(tagValue);
        }
    }

    /**
     * Multiple value tag constructor without empty tag value
     * @param tagName the tag name of a tag
     */
    public MultipleValueTag(String tagName){
        this(tagName, "");
    }

    /**
     * getter method for tagValues
     * @return tag values
     */
    public ArrayList<String> getTagValues() {
        return this.tagValues;
    }

    /**
     * Get the first tag value of a multiple value tag
     * @return the first tag value of a multiple value tag
     */
    public String getTagValue(){
        if(tagValues.size() == 0){
            return "";
        }
        else{
            return this.tagValues.get(0);
        }
    }

    /**
     * Combine two multiple value tag with the same tag name
     * @param tagValue input tag value
     */
    public void addTagValue(String tagValue){
        if(this.tagValues.contains(tagValue)){
            throw new RepeatedTagValueException("The tag (" + this.tagName + "," + tagValue + ") already exists.");
        }
        else{
            tagValues.add(tagValue);
        }
    }

    /**
     * toString method
     * @return string value of the combination of tagName and tagValue
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if(this.tagValues.size() == 0){
            return "(" + this.getTagName() + ", )";
        }
        for(int i = 0; i < this.tagValues.size(); i++){
            String tagValue = this.tagValues.get(i);
            result.append("(").append(this.tagName).append(", ").append(tagValue).append(")");
            if(i != this.tagValues.size() - 1){
                result.append(", ");
            }
        }
        return result.toString();
    }

    /**
     * Method to judge if two multiple value tag intercept
     * @param o another object
     * @return true they have some same combination
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o instanceof UniqueValueTag){
            return ((UniqueValueTag) o).tagName.equals(this.tagName);
        }
        else if(getClass() != this.getClass()){
            return false;
        }
        MultipleValueTag that = (MultipleValueTag) o;
        if(this.tagName.equals(that.tagName)){
            for(String tagValue : this.tagValues){
                if(that.tagValues.contains(tagValue)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Hash method
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValues);
    }
}
