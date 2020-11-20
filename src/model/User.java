package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * User class which is a subclass of Account
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class User extends Account{

    /**
     * albums created by user
     */
    private ArrayList<Album> albumList = new ArrayList<>();
    /**
     * Tags added by user
     */
    private ArrayList<Tag> createdTags = new ArrayList<>();
    /**
     * Photos added by user
     */
    private ArrayList<Photo> photos = new ArrayList<>();

    /**
     * The directory where the serialized data is stored
     */
    public static final String storeDir = "local/user";

    /**
     * Throws when the user try to create an album with existed album name
     */
    public static class RepeatedAlbumException extends RuntimeException {
        public RepeatedAlbumException(String s) {
            super(s);
        }
    }

    /**
     * Throws when the user try to find a album that doesn't exist
     */
    public static class AlbumNotExistedException extends RuntimeException{
        public AlbumNotExistedException(String s){
            super(s);
        }
    }

    /**
     * Throws when error occurs during serialization
     */
    public static class SerializationException extends RuntimeException{
        public SerializationException(String s){
            super(s);
        }
    }

    /**
     * Throws when error occurs during deserialization
     */
    public static class DeserializationException extends RuntimeException{
        public DeserializationException(String s){
            super(s);
        }
    }

    /**
     * The construct of an User instance with both username and password
     * @param username input username
     * @param password input password
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.createdTags.add(new UniqueValueTag("location", ""));
        this.createdTags.add(new MultipleValueTag("person", ""));
    }

    /**
     * The Construct of an User instance with only username
     * @param username input username
     */
    public User(String username){
        this(username, "");
    }

    /**
     * Deserialization method
     * @param username input username
     * @return the User instance
     * @throws FileNotFoundException throws if the serialized data cannot be found
     */
    public static User readData(String username) throws FileNotFoundException {
        ObjectInputStream ois;
        User user;
        File userDir = new File(storeDir);
        for(File userFile : Objects.requireNonNull(userDir.listFiles())){
            if(userFile.getName().equals(username + ".dat")){
                try{
                    ois = new ObjectInputStream(new FileInputStream(userFile.getAbsolutePath()));
                    user = (User)ois.readObject();
                    ois.close();
                    return user;
                }
                catch (Exception e){
                    throw new DeserializationException("Cannot deserialize " + userFile.getAbsolutePath() + ".");
                }
            }
        }
        throw new FileNotFoundException();
    }

    /**
     * Serialization method
     * @param user Target class
     * @return true if there is no issue during serialization
     */
    public static boolean writeData(User user){
        ObjectOutputStream oos;
        String address = storeDir + File.separator + user.username + ".dat";
        try{
            oos = new ObjectOutputStream(new FileOutputStream(address));
            oos.writeObject(user);
            oos.close();
            return true;
        }
        catch(Exception e){
            throw new SerializationException("Cannot serialize the User instance " + user.username + ".");
        }
    }

    /**
     * Getter method to get all the album of a user
     * @return a list of albums
     */
    public ArrayList<Album> getAlbumList(){
        return this.albumList;
    }

    /**
     * Get both existed tags and preset tags
     * @return a list of tags
     */
    public ArrayList<Tag> getCreatedTags(){
        return this.createdTags;
    }

    /**
     * Getter method to get current added photos
     * @return a list of phoeos
     */
    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    /**
     * Create an album and add the photos in it
     * @param album the album
     * @param photoList the photos that will be added in the new album
     */
    public void createAlbum(Album album, ArrayList<Photo> photoList){
        if (photoList != null){
            album.photoList = photoList;
        }
        if(this.albumList.contains(album)){
            throw new RepeatedAlbumException("The album \"" + username + "\" already exists.");
        }
        else{
            this.albumList.add(album);
        }
    }

    /**
     * Create an new album and add the photos in it
     * @param name the name of the new album
     * @param photoList the photos in the new album
     */
    public void createAlbum(String name, ArrayList<Photo> photoList){
        createAlbum(new Album(name), photoList);
    }

    /**
     * create an album
     * @param name the name of the new album
     */
    public void createAlbum(String name){
        createAlbum(name, null);
    }

    /**
     * Delete an album
     * @param album target album
     */
    public void deleteAlbum(Album album){
        this.albumList.remove(album);
    }

    /**
     * Rename an album
     * @param album target album
     * @param name new name
     */
    public void renameAlbum(Album album, String name){
        if(this.albumList.contains(album)){
            if(this.albumList.contains(new Album(name))){
                throw new RepeatedAlbumException("The album " + name + " is already existed.");
            }
            else{
                album.setName(name);
            }
        }
        else{
            throw new AlbumNotExistedException("The album " + album.name + " is not existed.");
        }
    }

    /**
     * Add a photo to an album
     * @param album target album
     * @param photo target photo
     */
    public void addPhoto(Album album, Photo photo){
        int index = this.photos.indexOf(photo);
        if (index < 0) {
            this.photos.add(photo);
            index = this.photos.indexOf(photo);
        }
        album.addPhoto(this.photos.get(index));
    }

    /**
     * Delete a photo from an album
     * @param album target album
     * @param photo target photo
     */
    public void deletePhoto(Album album, Photo photo){
        album.deletePhoto(photo);
        boolean albumContainsPhoto = false;
        for(Album eachAlbum : this.albumList){
            if (eachAlbum.photoList.contains(photo)) {
                albumContainsPhoto = true;
                break;
            }
        }
        if(!albumContainsPhoto){
            this.photos.remove(photo);
        }
    }

    /**
     * Caption a photo
     * @param photo target photo
     * @param caption new caption
     */
    public void captionPhoto(Photo photo, String caption){
        photo.setCaption(caption);
    }

    /**
     * Add a tag to a photo
     * @param tag added tag
     * @param photo target photo
     */
    public void addTagToPhoto(Tag tag, Photo photo){
        if(tag instanceof MultipleValueTag){
            MultipleValueTag multipleValueTag = (MultipleValueTag) tag;
            photo.addMultipleValueTag(multipleValueTag);
        }
        else if(tag instanceof UniqueValueTag){
            UniqueValueTag uniqueValueTag = (UniqueValueTag) tag;
            photo.addUniqueValueTag(uniqueValueTag);
        }
    }

    /**
     * Delete a tag of a photo
     * @param tag input tag
     * @param photo target photo
     */
    public void deletePhotoTag(Tag tag, Photo photo){
        if(tag instanceof MultipleValueTag){
            MultipleValueTag multipleValueTag = (MultipleValueTag) tag;
            photo.deleteMultipleValueTag(multipleValueTag);
            boolean containTag = false;
            for(Photo existedPhoto : this.photos){
                if(existedPhoto.tagList.contains(tag)){
                    containTag = true;
                    break;
                }
            }
            if(!containTag && !tag.getTagName().equals("name") && !tag.getTagName().contains("location")){
                this.createdTags.remove(tag);
            }
        }
        else if(tag instanceof UniqueValueTag){
            UniqueValueTag uniqueValueTag = (UniqueValueTag) tag;
            photo.deleteUniqueValueTag(uniqueValueTag);
            boolean containTag = false;
            for(Photo existedPhoto : this.photos){
                if(existedPhoto.tagList.contains(tag)){
                    containTag = true;
                    break;
                }
            }
            if(!containTag && !tag.getTagName().equals("name") && !tag.getTagName().contains("location")){
                this.createdTags.remove(tag);
            }
        }
    }

    /**
     * Copy a photo to an album
     * @param photo target photo
     * @param targetAlbum target album
     */
    public void copyPhoto(Photo photo, Album targetAlbum){
        targetAlbum.addPhoto(photo);
    }

    /**
     * Move a photo to another album
     * @param photo target photo
     * @param sourceAlbum source album
     * @param targetAlbum target album
     */
    public void movePhoto(Photo photo, Album sourceAlbum, Album targetAlbum){
        targetAlbum.addPhoto(photo);
        sourceAlbum.deletePhoto(photo);
    }

    /**
     * Search photo by a date range
     * @param earliestDate earliest date
     * @param latestDate latest date
     * @return a list of photos within the date range
     */
    public ArrayList<Photo> searchPhotoByDate(Date earliestDate, Date latestDate){
        if(this.albumList.size() == 0){
            return new ArrayList<>();
        }
        ArrayList<Photo> result = new ArrayList<>();
        for(Album album : this.albumList){
            ArrayList<Photo> albumResult = album.getPhotosByDateRange(earliestDate, latestDate);
            if(albumResult != null){
                for(Photo photo : albumResult){
                    if(!result.contains(photo)){
                        result.add(photo);
                    }
                }
            }
        }
        return result.size() == 0 ? new ArrayList<>() : result;
    }

    /**
     * Search photo by two tags
     * @param tag1 first tag
     * @param tag2 second tag
     * @param disjunctionFlag flag whether it is disjunction or conjunction
     * @return a list of photos
     */
    public ArrayList<Photo> searchPhotoByTags(Tag tag1, Tag tag2, boolean disjunctionFlag){
        if(this.albumList.size() == 0){
            return new ArrayList<>();
        }
        ArrayList<Photo> result = new ArrayList<>();
        for(Album album : this.albumList){
            ArrayList<Photo> tag1Photos = album.getPhotosByTag(tag1);
            ArrayList<Photo> tag2Photos = album.getPhotosByTag(tag2);
            ArrayList<Photo> albumResult = new ArrayList<>();
            if(disjunctionFlag){
                for(Photo photo : tag1Photos){
                    if(photo.hasTag(tag2)){
                        albumResult.add(photo);
                    }
                }
                for(Photo photo : tag2Photos){
                    if(photo.hasTag(tag1) && !result.contains(photo)){
                        albumResult.add(photo);
                    }
                }
            }
            else{
                if(tag1Photos != null){
                    albumResult.addAll(tag1Photos);
                }
                if(tag2 != null){
                    for(Photo photo : tag2Photos){
                        if(!albumResult.contains(photo)){
                            albumResult.add(photo);
                        }
                    }
                }
            }

            for(Photo photo : albumResult){
                if(!result.contains(photo)){
                    result.add(photo);
                }
            }
        }

        return result.size() == 0 ? new ArrayList<>() : result;
    }

    /**
     * search photo by a single tag
     * @param tag input tag
     * @return a list of photo such this condition
     */
    public ArrayList<Photo> searchPhotoByTag(Tag tag){
        return searchPhotoByTags(tag, null, false);
    }

    /**
     * Check if two object are the same
     * @param o another object
     * @return true if two objects are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * hash method
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password, albumList);
    }

    /**
     * toString method
     * @return the String value of username and password
     */
    @Override
    public String toString() {
        return "(" + this.username + ", " + this.password +")";
    }
}