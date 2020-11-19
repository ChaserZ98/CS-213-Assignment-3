package model;

import java.util.Objects;

/**
 * The UniqueValueTag class which is a subclass of Tag
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class UniqueValueTag extends Tag{
    /**
     * Tag value
     */
    String tagValue;

    /**
     * Throws when trying to add two same tagName-tagValue combination
     */
    public static class RepeatedTagValueException extends RuntimeException{
        public RepeatedTagValueException(String s){
            super(s);
        }
    }

    /**
     * Constructor for UniqueValueTag
     * @param tagName input tag name
     * @param tagValue input tag value
     */
    public UniqueValueTag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * Getter method to get the value of a tag
     * @return the value of a tag
     */
    public String getTagValue() {
        return this.tagValue;
    }

    /**
     * toString method to print the String value for UniqueValueTag
     * @return the String value of a UniqueValueTag;
     */
    @Override
    public String toString() {
        return "(" + this.tagName + ", " + this.tagValue + ")";
    }

    /**
     * Check if two UniqueValueTag are same
     * @param o another object
     * @return true if they are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o instanceof MultipleValueTag){
            return ((MultipleValueTag) o).tagName.equals(this.tagName);
        }
        else if(getClass() != o.getClass()){
            return false;
        }
        UniqueValueTag that = (UniqueValueTag) o;
        return Objects.equals(tagValue, that.tagValue) && Objects.equals(tagName, that.tagName);
    }

    /**
     * hash method
     * @return hascode
     */
    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValue);
    }
}
