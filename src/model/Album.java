package model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Album implements Serializable {
    String name;
    ArrayList<Photo> photoList;

    private static class PhotoExistedException extends RuntimeException{
        public PhotoExistedException(String s) {
            super(s);
        }
    }

    private static class PhotoNotFoundException extends RuntimeException{
        public PhotoNotFoundException(String s){
            super(s);
        }
    }

    public Album(String name){
        this.name = name;
        photoList = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public void addPhoto(Photo photo){
        if(this.photoList.contains(photo)){
            throw new PhotoExistedException("The photo " + photo.address + " is already in the album.");
        }
        else{
            this.photoList.add(photo);
        }
    }

    public void deletePhoto(Photo photo){
        if(this.photoList.contains(photo)){
            this.photoList.remove(photo);
        }
        else{
            throw new PhotoNotFoundException("The photo " + photo.address + " is not in the album.");
        }
    }

//    public ArrayList<String> getDistinctTagNames(){
//        ArrayList<String> result = new ArrayList<>();
//        for(Photo photo : photoList){
//            for(Tag tag : photo.tagList){
//                if(result.contains(tag.tagName))
//            }
//        }
//    }

    public ArrayList<Photo> getPhotosByDateRange(Date earliestDate, Date latestDate){
        ArrayList<Photo> result = new ArrayList<>();
        for(Photo photo : this.photoList){
            if(photo.inDateRange(earliestDate, latestDate)){
                result.add(photo);
            }
        }
        return result.size() == 0 ?  null : result;
    }

    public ArrayList<Photo> getPhotosByTag(Tag tag){
        ArrayList<Photo> result = new ArrayList<>();
        for(Photo photo : this.photoList){
            if(photo.hasTag(tag)){
                result.add(photo);
            }
        }
        return result.size() == 0 ? null : result;
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

    @Override
    public String toString() {
        return this.name;
    }
}