package model;

import java.util.ArrayList;
import java.util.Objects;

public class MultipleValueTag extends Tag{
    ArrayList<String> tagValues;

    private static class RepeatedTagValueException extends RuntimeException{
        public RepeatedTagValueException(String s){
            super(s);
        }
    }

    public MultipleValueTag(String tagName){
        this.tagName = tagName;
        tagValues = new ArrayList<>();
    }

    public void addTagValue(String tagValue){
        if(this.tagValues.contains(tagValue)){
            throw new RepeatedTagValueException("The tag (" + this.tagName + "," + tagValue + ") already exists.");
        }
        else{
            tagValues.add(tagValue);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(String tagValue : this.tagValues){
            result.append("(").append(this.tagName).append(", ").append(tagValue).append(")");
        }
        return result.toString();
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValues);
    }
}
