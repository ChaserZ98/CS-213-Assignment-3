package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends Account{
    ArrayList<User> userList;

    public Admin(){
        this.username = "admin";
        this.password = "admin";
        this.userList = new ArrayList<>();
    }

    private static class RepeatedUserException extends RuntimeException {
        public RepeatedUserException(String s) {
            super(s);
        }
    }

    private static class UserNotExistedException extends RuntimeException {
        public UserNotExistedException(String s) {
            super(s);
        }
    }

    private ArrayList<User> getUserList(){
        return userList;
    }

    private void createNewUser(String username, String password) throws RepeatedUserException {
        if(username.equals("admin")){
            throw new RepeatedUserException("The username \"" + username + "\" already exists.");
        }
        User newUser = new User(username, password);
        if(this.userList.contains(newUser)){
            throw new RepeatedUserException("The username \"" + username + "\" already exists.");
        }
        else{
            userList.add(newUser);
        }
    }

    private void deleteUser(String username) throws UserNotExistedException{
        User targetUser = new User(username);
        if(this.userList.contains(targetUser)){
            userList.remove(targetUser);
        }
        else{
            throw new UserNotExistedException("The user \"" + username + "\" doesn't exist.");
        }
    }
}
