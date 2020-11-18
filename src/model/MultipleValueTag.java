package model;

import java.util.ArrayList;
import java.util.Objects;

public class MultipleValueTag extends Tag{
    String tagValue;
    ArrayList<String> tagValues;

    private static class RepeatedTagValueException extends RuntimeException{
        public RepeatedTagValueException(String s){
            super(s);
        }
    }

    public MultipleValueTag(String tagName, String tagValue){
        this.tagName = tagName;
        tagValues = new ArrayList<>();
        if(tagValue.length() != 0){
            tagValues.add(tagValue);
        }
    }

    public MultipleValueTag(String tagName){
        this(tagName, "");
    }

    public ArrayList<String> getTagValues() {
        return this.tagValues;
    }

    public String getTagValue(){
        if(tagValues.size() == 0){
            return "";
        }
        else{
            return this.tagValues.get(0);
        }
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
