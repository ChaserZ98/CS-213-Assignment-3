package model;

public abstract class Account{
    protected String username;
    protected String password;

    public String getUsername() {
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
}
