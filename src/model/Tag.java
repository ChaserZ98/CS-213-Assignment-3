package model;

import java.io.Serializable;

/**
 * Tag abstract class
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public abstract class Tag implements Serializable {
    /**
     * Tag name os a tag
     */
    public String tagName;

    /**
     * equals method
     * @param o another object
     * @return true if two tag are same
     */
    public abstract boolean equals(Object o);

    /**
     * getter method for tagName
     * @return the tagName of a tag
     */
    public String getTagName() {
        return this.tagName;
    }
}
