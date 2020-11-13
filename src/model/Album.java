package model;

import java.util.ArrayList;
import java.util.Objects;

public class Album{
    String name;
    ArrayList<Photo> photoList;

    public Album(String name){
        this.name = name;
        photoList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}