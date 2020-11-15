package model;

import java.io.*;
import java.util.ArrayList;

public class Admin extends Account{
    ArrayList<User> userList;

    public static final String storeDir = "local/admin";
    public static final String storeFile = "admin.dat";

    public Admin(){
        this.username = "admin";
        this.password = "admin";
        this.userList = new ArrayList<>();
    }

    public static class RepeatedUserException extends RuntimeException {
        public RepeatedUserException(String s) {
            super(s);
        }
    }

    public static class UserNotExistedException extends RuntimeException {
        public UserNotExistedException(String s) {
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

    public static Admin readData(){
        ObjectInputStream ois;
        Admin admin;
        String address = storeDir + File.separator + storeFile;
        try{
            ois = new ObjectInputStream(new FileInputStream(address));
            admin = (Admin)ois.readObject();
        }
        catch(Exception e){
            throw new DeserializationException("Cannot deserialize " + address + ".");
        }
        return admin;
    }

    public static boolean writeData(Admin admin){
        ObjectOutputStream oos;
        String address = storeDir + File.separator + storeFile;
        try{
            oos = new ObjectOutputStream(new FileOutputStream(address));
            oos.writeObject(admin);
            return true;
        }
        catch(Exception e){
            throw new SerializationException("Cannot serialize the Admin instance " + admin.username + ".");
        }
    }

    public ArrayList<User> getUserList(){
        return userList;
    }

    public void createNewUser(String username, String password) throws RepeatedUserException{
        if(username.equals("admin")){
            throw new RepeatedUserException("The username \"" + username + "\" already exists.");
        }
        User newUser = new User(username, password);
        if(this.userList.contains(newUser)){
            throw new RepeatedUserException("The username \"" + username + "\" already exists.");
        }
        else{
            this.userList.add(newUser);
        }
    }

    public void deleteUser(String username) throws UserNotExistedException{
        User targetUser = new User(username);
        if(this.userList.contains(targetUser)){
            userList.remove(targetUser);
            File file = new File("local/user/" + targetUser.getUsername() + ".dat");
            if(file.exists()){
                file.delete();
            }
        }
        else{
            throw new UserNotExistedException("The user \"" + username + "\" doesn't exist.");
        }
    }
}
