package model;

import java.io.Serializable;

/**
 * abstract class for account
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */
public abstract class Account implements Serializable {
    protected String username;
    protected String password;

    /**
     * get the username
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * get the password
     * @return  password
     */
    public String getPassword(){
        return this.password;
    }
}
