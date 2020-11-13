package model;

import java.util.Objects;

public class Tag {
    String tagName;
    String tagValue;

    public Tag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName) &&
                Objects.equals(tagValue, tag.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValue);
    }
}
