package com.pes.fibness;

import java.io.Serializable;

public class UserModel implements Serializable, Comparable<UserModel>{

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


    /*use to sort list by username*/
    @Override
    public int compareTo(UserModel userModel) {
        return this.username.compareTo(userModel.username);
    }
}
