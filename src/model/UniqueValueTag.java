package model;

import java.awt.image.MultiPixelPackedSampleModel;
import java.util.ArrayList;
import java.util.Objects;

public class UniqueValueTag extends Tag{
    String tagValue;

    private static class RepeatedTagValueException extends RuntimeException{
        public RepeatedTagValueException(String s){
            super(s);
        }
    }

    public UniqueValueTag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return this.tagValue;
    }

    @Override
    public String toString() {
        return "(" + this.tagName + ", " + this.tagValue + ")";
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValue);
    }
}
