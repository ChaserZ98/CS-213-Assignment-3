package model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Album implements Serializable, Comparable<Album>{
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

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Photo> getPhotoList(){
        return this.photoList;
    }

    public int getNumberOfPhotos(){
        return this.photoList.size();
    }

    public Date getEarliestDate(){
        if(this.photoList.size() == 0){
            return null;
        }
        else{
            Date earliestDate = this.photoList.get(0).date;
            for(Photo photo : this.photoList){
                if(photo.date.getTime() < earliestDate.getTime()){
                    earliestDate = photo.date;
                }
            }
            return earliestDate;
        }
    }

    public Date getLatestDate(){
        if(this.photoList.size() == 0){
            return null;
        }
        else{
            Date latestDate = this.photoList.get(0).date;
            for(Photo photo : this.photoList){
                if(photo.date.getTime() > latestDate.getTime()){
                    latestDate = photo.date;
                }
            }
            return latestDate;
        }
    }

    public String[] getDateRange(){
        return getDateRange(new SimpleDateFormat("yyyy-MM-dd"));
    }

    public String[] getDateRange(DateFormat dateFormat){
        if(this.photoList.size() == 0){
            return null;
        }
        String[] result = new String[2];
        result[0] = dateFormat.format(getEarliestDate());
        result[1] = dateFormat.format(getLatestDate());

        return result;
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

    @Override
    public int compareTo(Album newAlbum){
        return this.name.compareTo(newAlbum.getName());
    }
}