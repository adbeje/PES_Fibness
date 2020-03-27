package com.pes.fibness;

import java.util.AbstractQueue;
import java.util.ArrayList;

public class User {
    private static ArrayList<String> TrainingList = new ArrayList<String>();;

    public User(){
        for(int i = 0; i < 24; i++){
            TrainingList.add("Training " + (i+1));
        }
    }

    public static void setNewTraining(String name){
        TrainingList.add(name);
    }

    public ArrayList<String> getTrainingList(){
        return TrainingList;
    }

}
