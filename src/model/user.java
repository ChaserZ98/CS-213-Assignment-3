package model;

import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable {

    private static final long serialVersionUID = -7375842930033306561L;

    //A user's information

    String username = "";
    String password = "";
    public ArrayList<album> albums = new ArrayList<album>();

    public user(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setNamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ArrayList<album> getAlbums(){
        return albums;
    }

}