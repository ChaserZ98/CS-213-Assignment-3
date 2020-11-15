package model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Tag implements Serializable {
    public String tagName;

    public abstract boolean equals(Object o);
}
