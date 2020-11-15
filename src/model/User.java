package model;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User extends Account{

    //A user's information
    private ArrayList<Album> albumList = new ArrayList<>();

    public static final String storeDir = "local/user";

    private static class RepeatedAlbumException extends RuntimeException {
        public RepeatedAlbumException(String s) {
            super(s);
        }
    }

    private static class AlbumNotExistedException extends RuntimeException{
        public AlbumNotExistedException(String s){
            super(s);
        }
    }

    private static class SerializationException extends RuntimeException{
        public SerializationException(String s){
            super(s);
        }
    }

    private static class DeserializationException extends RuntimeException{
        public DeserializationException(String s){
            super(s);
        }
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String username){
        this(username, "");
    }

    public static User readData(String username) throws FileNotFoundException {
        ObjectInputStream ois;
        User user;
        String address = storeDir + File.separator + username + ".dat";
        File file = new File(address);
        if(!file.exists()){
            throw new FileNotFoundException("Cannot find file " + file.getAbsolutePath());
        }
        try{
            ois = new ObjectInputStream(new FileInputStream(address));
            user = (User)ois.readObject();
        }
        catch (Exception e){
            throw new DeserializationException("Cannot deserialize " + address + ".");
        }
        return user;
    }

    public static boolean writeData(User user){
        ObjectOutputStream oos;
        String address = storeDir + File.separator + user.username + ".dat";
        try{
            oos = new ObjectOutputStream(new FileOutputStream(address));
            oos.writeObject(user);
            return true;
        }
        catch(Exception e){
            throw new SerializationException("Cannot serialize the User instance " + user.username + ".");
        }
    }

    public ArrayList<Album> getAlbumList(){
        return this.albumList;
    }

    public void createAlbum(String name, ArrayList<Photo> photoList){
        Album newAlbum = new Album(name);
        if(photoList != null){
            newAlbum.photoList = photoList;
        }
        if(this.albumList.contains(newAlbum)){
            throw new RepeatedAlbumException("The album \"" + username + "\" already exists.");
        }
        else{
            this.albumList.add(newAlbum);
        }
    }

    public void createAlbum(String name){
        createAlbum(name, null);
    }

    public void deleteAlbum(Album album){
        this.albumList.remove(album);
    }

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

    public void captionPhoto(Photo photo, String caption){
        photo.setCaption(caption);
    }

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

    public void deletePhotoTag(Tag tag, Photo photo){
        if(tag instanceof MultipleValueTag){
            MultipleValueTag multipleValueTag = (MultipleValueTag) tag;
            photo.deleteMultipleValueTag(multipleValueTag);
        }
        else if(tag instanceof UniqueValueTag){
            UniqueValueTag uniqueValueTag = (UniqueValueTag) tag;
            photo.deleteUniqueValueTag(uniqueValueTag);
        }
    }

    public void copyPhoto(Photo photo, Album targetAlbum){
        targetAlbum.addPhoto(photo);
    }

    public void movePhoto(Photo photo, Album sourceAlbum, Album targetAlbum){
        targetAlbum.addPhoto(photo);
        sourceAlbum.deletePhoto(photo);
    }

    public ArrayList<Photo> searchPhotoByDate(Album album, Date earliestDate, Date latestDate){
        return album.getPhotosByDateRange(earliestDate, latestDate);
    }

    public ArrayList<Photo> searchPhotoByTags(Album album, Tag tag1, Tag tag2, boolean disjunctionFlag){
        ArrayList<Photo> result = new ArrayList<>();
        ArrayList<Photo> tag1Photos = album.getPhotosByTag(tag1);
        ArrayList<Photo> tag2Photos = album.getPhotosByTag(tag2);
        if(disjunctionFlag){
            for(Photo photo : tag1Photos){
                if(photo.hasTag(tag2)){
                    result.add(photo);
                }
            }
            for(Photo photo : tag2Photos){
                if(photo.hasTag(tag1) && !result.contains(photo)){
                    result.add(photo);
                }
            }
        }
        else{
            if(tag1Photos != null){
                result.addAll(tag1Photos);
            }
            if(tag2 != null){
                for(Photo photo : tag2Photos){
                    if(!result.contains(photo)){
                        result.add(photo);
                    }
                }
            }
        }
        return result.size() == 0 ? null : result;
    }

    public ArrayList<Photo> searchPhotoByTag(Album album, Tag tag){
        return searchPhotoByTags(album, tag, null, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, albumList);
    }

    @Override
    public String toString() {
        return "(" + this.username + ", " + this.password +")";
    }
}