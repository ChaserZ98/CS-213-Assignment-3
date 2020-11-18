package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User extends Account{

    //A user's information
    private ArrayList<Album> albumList = new ArrayList<>();

    public static final String storeDir = "local/user";

    public static class RepeatedAlbumException extends RuntimeException {
        public RepeatedAlbumException(String s) {
            super(s);
        }
    }

    public static class AlbumNotExistedException extends RuntimeException{
        public AlbumNotExistedException(String s){
            super(s);
        }
    }

    public static class SerializationException extends RuntimeException{
        public SerializationException(String s){
            super(s);
        }
    }

    public static class DeserializationException extends RuntimeException{
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
        File userDir = new File(storeDir);
        for(File userFile : Objects.requireNonNull(userDir.listFiles())){
            if(userFile.getName().equals(username + ".dat")){
                try{
                    ois = new ObjectInputStream(new FileInputStream(userFile.getAbsolutePath()));
                    user = (User)ois.readObject();
                    return user;
                }
                catch (Exception e){
                    throw new DeserializationException("Cannot deserialize " + userFile.getAbsolutePath() + ".");
                }
            }
        }
        throw new FileNotFoundException();
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

    public ArrayList<Photo> searchPhotoByDate(Date earliestDate, Date latestDate){
        if(this.albumList.size() == 0){
            return null;
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
        return result.size() == 0 ? null : result;
    }

    public ArrayList<Photo> searchPhotoByTags(Tag tag1, Tag tag2, boolean disjunctionFlag){
        if(this.albumList.size() == 0){
            return null;
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

        return result.size() == 0 ? null : result;
    }

    public ArrayList<Photo> searchPhotoByTag(Tag tag){
        return searchPhotoByTags(tag, null, false);
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