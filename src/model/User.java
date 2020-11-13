package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User extends Account {

    //A user's information
    ArrayList<Album> albumList = new ArrayList<>();

    private static class RepeatedAlbumException extends RuntimeException {
        public RepeatedAlbumException(String s) {
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

    public ArrayList<Album> getAlbumList(){
        return this.albumList;
    }

    public void createAlbum(String name){
        Album newAlbum = new Album(name);
        if(this.albumList.contains(newAlbum)){
            throw new RepeatedAlbumException("The album \"" + username + "\" already exists.");
        }
        else{
            albumList.add(newAlbum);
        }
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
}