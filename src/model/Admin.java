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
    ArrayList<User> userList;

    /**
     * create the final String value storeDir
     */
    public static final String storeDir = "local/admin";

    /**
     * create the final String value storeFile
     */
    public static final String storeFile = "admin.dat";

    /**
     * the method to create admin account and password
     */
    public Admin(){
        this.username = "admin";
        this.password = "admin";
        this.userList = new ArrayList<>();
    }

    /**
     * Check Repeat User
     */
    public static class RepeatedUserException extends RuntimeException {
        public RepeatedUserException(String s) {
            super(s);
        }
    }

    /**
     * Check if the User Existed
     */
    public static class UserNotExistedException extends RuntimeException {
        public UserNotExistedException(String s) {
            super(s);
        }
    }

    /**
     * Serialization
     */
    public static class SerializationException extends RuntimeException{
        public SerializationException(String s){
            super(s);
        }
    }

    /**
     * Deserialization
     */
    public static class DeserializationException extends RuntimeException{
        public DeserializationException(String s){
            super(s);
        }
    }

    /**
     * read the data for admin
     * @return admin
     * @throws FileNotFoundException
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
     * Write data for admin
     * @param admin admin
     * @return boolean value
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
     * get UserList
     * @return userList
     */
    public ArrayList<User> getUserList(){
        return userList;
    }

    /**
     * use admin account to create new user
     * @param username String variable for username
     * @param password String variable for password
     * @throws RepeatedUserException
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
     * use admin account to delete exist user
     * @param username String value for username
     * @throws UserNotExistedException
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
