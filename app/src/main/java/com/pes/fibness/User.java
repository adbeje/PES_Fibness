package com.pes.fibness;


import android.media.Image;

import java.util.Date;
import java.util.ArrayList;

public class User {

    private Integer user_id;
    private String name;
    private Image image;
    private int nFollower;
    private int nFollowing;
    private int nPost;
    private Date registerDate;
    private String country;
    private String profileType;
    private static ArrayList<String> TrainingList = new ArrayList<>();
    private boolean[] settings = new boolean[5]; /*0 = Age, 1 = Distance, 2 = Invitation , 3 = Follower, 4 = Message*/
    private String recoveryCode;


    /*we are applying singleton because we will have an instance for each aplication user*/
    private static User instance = null;
    private User(){
        image = null; recoveryCode = "";
        for(int i = 0; i < 24; i++){
            TrainingList.add("Training " + (i+1));
        }
    }

    public static User getInstances(){
        if(instance == null)
            instance = new User();
        return instance;
    }


    public static void setNewTraining(String name){
        TrainingList.add(name);
    }


    public ArrayList<String> getTrainingList(){
        return TrainingList;
    }

    public Integer getId() { return  this.user_id; }
    public void setId(Integer id) { this.user_id = id; }

    public boolean[] getSettings(){
        return settings;
    }
    public void setSettings(boolean[] s){this.settings = s;}


    public String getRecoveryCode(){return this.recoveryCode;}
    public void setRecoveryCode(String s) {this.recoveryCode = s;}



    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }

    public int getnFollower() { return nFollower; }
    public void setnFollower(int nFollower) { this.nFollower = nFollower; }

    public int getnFollowing() { return nFollowing; }
    public void setnFollowing(int nFollowing) { this.nFollowing = nFollowing; }

    public int getnPost() { return nPost; }
    public void setnPost(int nPost) { this.nPost = nPost; }



}
