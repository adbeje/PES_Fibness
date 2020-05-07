package com.pes.fibness;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String username;

    public UserModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
