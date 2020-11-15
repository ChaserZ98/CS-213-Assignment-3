package model;

import java.io.Serializable;

public abstract class Account implements Serializable {
    protected String username;
    protected String password;

    public String getUsername() {
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
