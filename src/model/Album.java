package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Album class
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class Album implements Serializable, Comparable<Album>{
    /**
     * Album name
     */
    String name;
    /**
     * photos in the album
     */
    ArrayList<Photo> photoList;

    /**
     * Throws when user tries to add a existed photo into an album
     */
    public static class PhotoExistedException extends RuntimeException{
        public PhotoExistedException(String s) {
            super(s);
        }
    }

    /**
     * Throws when user try to access a photo that is not in the album
     */
    public static class PhotoNotFoundException extends RuntimeException{
        public PhotoNotFoundException(String s){
            super(s);
        }
    }

    /**
     * Album constructor
     * @param name the name of the album
     */
    public Album(String name){
        this.name = name;
        photoList = new ArrayList<>();
    }

    /**
     * Getter method for album name
     * @return album name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter mmethod for album name
     * @param name the new name of album
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Getter method for photos
     * @return Photo list of this album
     */
    public ArrayList<Photo> getPhotoList(){
        return this.photoList;
    }

    /**
     * Get the total number of photos in this album
     * @return total number of photos
     */
    public int getNumberOfPhotos(){
        return this.photoList.size();
    }

    /**
     * Get the earliest date in this album
     * @return the earliest data of a photo in this album
     */
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

    /**
     * Get the latest date in this album
     * @return the latest date of a photo in this album
     */
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

    /**
     * Get the date range of photos in this album
     * @return a string array that contains the earliest data and the latest date
     */
    public String[] getDateRange(){
        return getDateRange(new SimpleDateFormat("yyyy-MM-dd"));
    }

    /**
     * Polymorphism for getDateRange
     * @param dateFormat Dateformat
     * @return a String array that contains the earliest data and the latest date
     */
    public String[] getDateRange(DateFormat dateFormat){
        if(this.photoList.size() == 0){
            return null;
        }
        String[] result = new String[2];
        result[0] = dateFormat.format(getEarliestDate());
        result[1] = dateFormat.format(getLatestDate());

        return result;
    }

    /**
     * Add a photo to this album
     * @param photo The input photo
     */
    public void addPhoto(Photo photo){
        if(this.photoList.contains(photo)){
            throw new PhotoExistedException("The photo " + photo.address + " is already in the album.");
        }
        else{
            this.photoList.add(photo);
        }
    }

    /**
     * Delete a photo from this album
     * @param photo the photo that will be deleted
     */
    public void deletePhoto(Photo photo){
        if(this.photoList.contains(photo)){
            this.photoList.remove(photo);
        }
        else{
            throw new PhotoNotFoundException("The photo " + photo.address + " is not in the album.");
        }
    }

    /**
     * Methods to search photo within a specific date range
     * @param earliestDate Date variable
     * @param latestDate Date variable
     * @return photos which date is within the date range
     */
    public ArrayList<Photo> getPhotosByDateRange(Date earliestDate, Date latestDate){
        ArrayList<Photo> result = new ArrayList<>();
        for(Photo photo : this.photoList){
            if(photo.inDateRange(earliestDate, latestDate)){
                result.add(photo);
            }
        }
        return result.size() == 0 ?  null : result;
    }

    /**
     * Methods to search photo by a specific tag
     * @param tag Tag
     * @return photos which contains a specific tag
     */
    public ArrayList<Photo> getPhotosByTag(Tag tag){
        ArrayList<Photo> result = new ArrayList<>();
        for(Photo photo : this.photoList){
            if(photo.hasTag(tag)){
                result.add(photo);
            }
        }
        return result.size() == 0 ? new ArrayList<>() : result;
    }

    /**
     * equals method to check for equality
     * @param o comparator
     * @return true if two album have same album name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name);
    }

    /**
     * hash method
     * @return hascode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * toString method
     * @return the name of the album
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Compare two album by their names alphabetically
     * @param newAlbum another album
     * @return zero if they are same
     */
    @Override
    public int compareTo(Album newAlbum){
        return this.name.compareTo(newAlbum.getName());
    }
}