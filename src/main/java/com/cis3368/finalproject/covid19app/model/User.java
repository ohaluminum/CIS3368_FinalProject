package com.cis3368.finalproject.covid19app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    private final String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    //Default Constructor
    public User()
    {
        id = UUID.randomUUID().toString();
        username = "N/A";
        password = "N/A";
        isAdmin = true;
    }

    //Getter and Setter
    public String getID()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }
}
