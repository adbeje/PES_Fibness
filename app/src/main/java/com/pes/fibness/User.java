package com.pes.fibness;


import android.media.Image;
import java.util.Date;
import java.util.ArrayList;

public class User {

    private String email;
    private String name;
    private Date registerDate;
    private String province;
    private Image image;
    private String profileType;
    private String address;
    private static ArrayList<String> TrainingList = new ArrayList<>();
    /**we wil have event, activities, achievments, etc, all these information we have to save here to reduce BD connection**/

    /*we are applying singleton because we will have an instance for each aplication user*/
    private static User instance = null;
    private User(){
        email = ""; name = ""; registerDate = new Date(); province = ""; profileType = ""; address = "";
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

    /*we will need setter and getter*/



}
