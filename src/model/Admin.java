package model;

import java.io.*;
import java.util.ArrayList;
/**
 * class for account admin
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */
public class Admin extends Account{
    /**
     * ArrayList that contains users' info
     */
    ArrayList<User> userList;

    /**
     * The directory where the serialized data is stored
     */
    public static final String storeDir = "local/admin";

    /**
     * The file where the serialized data is stored
     */
    public static final String storeFile = "admin.dat";

    /**
     * No-arg constructor
     */
    public Admin(){
        this.username = "admin";
        this.password = "admin";
        this.userList = new ArrayList<>();
    }

    /**
     * Throws when there two user have same username
     */
    public static class RepeatedUserException extends RuntimeException {
        public RepeatedUserException(String s) {
            super(s);
        }
    }

    /**
     * Throws when a user cannot be find
     */
    public static class UserNotExistedException extends RuntimeException {
        public UserNotExistedException(String s) {
            super(s);
        }
    }

    /**
     * Throws when there is some error during serialization
     */
    public static class SerializationException extends RuntimeException{
        public SerializationException(String s){
            super(s);
        }
    }

    /**
     * Throws when there is some error during deserialization
     */
    public static class DeserializationException extends RuntimeException{
        public DeserializationException(String s){
            super(s);
        }
    }

    /**
     * The method to deserialize the admin class
     * @return stored admin instance
     * @throws FileNotFoundException throws there is some problem with the file address
     */
    public static Admin readData() throws FileNotFoundException {
        ObjectInputStream ois;
        Admin admin;
        String address = storeDir + File.separator + storeFile;
        File file = new File(address);
        if(!file.exists()){
            throw new FileNotFoundException("Cannot find file " + file.getAbsolutePath());
        }
        try{
            ois = new ObjectInputStream(new FileInputStream(address));
            admin = (Admin)ois.readObject();
            ois.close();
        }
        catch(Exception e){
            throw new DeserializationException("Cannot deserialize " + address + ".");
        }
        return admin;
    }

    /**
     * The method to serialize the admin class
     * @param admin the admin instance
     * @return true if the data is correctly stored
     */
    public static boolean writeData(Admin admin){
        ObjectOutputStream oos;
        String address = storeDir + File.separator + storeFile;
        try{
            oos = new ObjectOutputStream(new FileOutputStream(address));
            oos.writeObject(admin);
            oos.close();
            return true;
        }
        catch(Exception e){
            throw new SerializationException("Cannot serialize the Admin instance " + admin.username + ".");
        }
    }

    /**
     * getter method for userList
     * @return userList
     */
    public ArrayList<User> getUserList(){
        return userList;
    }

    /**
     * Called when the admin tries to create a new user
     * @param username String type
     * @param password String type
     * @throws RepeatedUserException throws when admin tries to create a user that already exists
     */
    public void createNewUser(String username, String password) throws RepeatedUserException{
        if(username.equals("admin")){
            throw new RepeatedUserException("The username " + username + " already exists.");
        }
        User newUser = new User(username, password);
        if(this.userList.contains(newUser)){
            throw new RepeatedUserException("The username " + username + " already exists.");
        }
        else{
            this.userList.add(newUser);
            try{
                User.writeData(newUser);
            }
            catch(User.SerializationException e){
                throw new SerializationException(e.getMessage());
            }
        }
    }

    /**
     * Delete user account
     * @param username String value for username
     * @throws UserNotExistedException throws when admin tries to delete a user that does not exist
     */
    public void deleteUser(String username) throws UserNotExistedException{
        User targetUser = new User(username);
        if(this.userList.contains(targetUser)){
            userList.remove(targetUser);
            File file = new File("local/user/" + targetUser.getUsername() + ".dat");
            if(file.exists()){
                boolean isDelete = file.delete();
            }
        }
        else{
            throw new UserNotExistedException("The user " + username + " doesn't exist.");
        }
    }
}
