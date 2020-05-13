package com.pes.fibness;


import com.mapbox.geojson.Point;
import java.util.Date;
import android.util.Pair;

import java.io.File;
import java.util.ArrayList;

public class User {

    private Integer user_id;

    /*User profile*/
    private String name, email, userType, profileType, gender, description, birthDate, registerDate, country, imageRoute;
    private int nFollower, nFollowing,  nPost;
    private byte[] image;
    private boolean[] settings = new boolean[5]; /*0 = Age, 1 = Distance, 2 = Invitation , 3 = Follower, 4 = Message*/
    private String recoveryCode;

    private String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};

    /*User fitness*/
    private static ArrayList<Training> trainingList = new ArrayList<>();
    private static ArrayList<Exercise> exerciseList = new ArrayList<>();
    private static ArrayList<Diet> dietList = new ArrayList<>();
    private static ArrayList<Meal> mealList = new ArrayList<>();
    private static ArrayList<Aliment> alimentList = new ArrayList<>();

    private static ArrayList<Ruta> rutasList = new ArrayList<>();

    private ArrayList<Achievement> achievements = new ArrayList<>(4);
    private ArrayList<Pair<Integer, String>> shortUsersInfo = new ArrayList<>();
    private UsersInfo selectedUser = new UsersInfo();
    private ArrayList<Pair<Integer, String>> userFollowers = new ArrayList<>();
    private ArrayList<Pair<Integer, String>> userFollowing = new ArrayList<>();

    /*we are applying singleton because we will have an instance for each aplication user*/
    private static User instance = null;
    private User(){
        image = null; recoveryCode = "";
        System.out.println("achievements size: " +  achievements.size());

    }

    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }


    public Integer getId() { return  this.user_id; }
    public void setId(Integer id) { this.user_id = id; }

    /*PROFILE*/
    public boolean[] getSettings(){ return settings; }
    public void setSettings(boolean[] s){this.settings = s;}

    public String getRecoveryCode(){return this.recoveryCode;}
    public void setRecoveryCode(String s) {this.recoveryCode = s;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public int getnFollower() { return nFollower; }
    public void setnFollower(int nFollower) { this.nFollower = nFollower; }

    public int getnFollowing() { return nFollowing; }
    public void setnFollowing(int nFollowing) { this.nFollowing = nFollowing; }

    public int getnPost() { return nPost; }
    public void setnPost(int nPost) { this.nPost = nPost; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getProfileType() { return profileType; }
    public void setProfileType(String profileType) { this.profileType = profileType; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getRegisterDate() { return registerDate; }
    public void setRegisterDate(String registerDate) { this.registerDate = registerDate; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getImageRoute() { return imageRoute; }
    public void setImageRoute(String imageRoute) { this.imageRoute = imageRoute; }

    /** Achievements **/
    public ArrayList<Achievement> getAchievements() { return achievements; }
    public void setAchievements(ArrayList<Achievement> achievements) { this.achievements = achievements; }

    /** Serach Users **/
    public ArrayList<Pair<Integer, String>> getShortUsersInfo() { return shortUsersInfo; }
    public void setShortUsersInfo(ArrayList<Pair<Integer, String>> shortUsersInfo) { this.shortUsersInfo = shortUsersInfo; }

    /*Selected Users*/
    public UsersInfo getSelectedUser() { return selectedUser; }
    public void setSelectedUser(UsersInfo selectedUser) { this.selectedUser = selectedUser; }

    /*User Followers*/
    public ArrayList<Pair<Integer, String>> getUserFollowers() { return userFollowers; }
    public void setUserFollowers(ArrayList<Pair<Integer, String>> userFollowers) { this.userFollowers = userFollowers; }

    /*User Following*/
    public ArrayList<Pair<Integer, String>> getUserFollowing() { return userFollowing; }
    public void setUserFollowing(ArrayList<Pair<Integer, String>> userFollowing) { this.userFollowing = userFollowing; }


    /***************************************************************************/
    /** Trainings **/
    public void setTrainingList(ArrayList<Training> t){
        trainingList = t;
    }

    public ArrayList<String> getTrainingList(){
        ArrayList<String> lista = new ArrayList<>();
        for(int i = 0; i < trainingList.size(); ++i) {
            lista.add(trainingList.get(i).name);
        }
        return lista;
    }

    public void addTraining(Training t){
        trainingList.add(t);
    }

    public void deleteTraining(String name){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                trainingList.remove(i);
                break;
            }
        }
    }

    public void setTrainingDesc(String name, String desc){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                trainingList.get(i).desc = desc;
                break;
            }
        }
    }

    public String getTrainingDesc(String name){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                return trainingList.get(i).desc;
            }
        }
        return "";
    }

    public void setTrainingName(String name, String newName){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                trainingList.get(i).name = newName;
                break;
            }
        }
    }

    public void setTrainingID(String name, int newID){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                trainingList.get(i).id = newID;
                break;
            }
        }
    }

    public int getTrainingID(String name){
        for(int i = 0; i < trainingList.size(); ++i){
            if(trainingList.get(i).name.equals(name)){
                return trainingList.get(i).id;
            }
        }
        return -1;
    }

    /** Exercises **/
    public ArrayList<Exercise> getExerciseList(){
        return exerciseList;
    }

    public void setExerciseList(ArrayList<Exercise> exercises){
        exerciseList = exercises;
    }

    public void addExercise(Exercise e){
        exerciseList.add(e);
    }

    public void deleteExercise(int pos){
        exerciseList.remove(pos);
    }

    public void updateExercise(int pos, Exercise e){
        exerciseList.set(pos, e);
    }

    public int sizeExerciseList(){return exerciseList.size();}

    public int getExerciseID(int position){ return exerciseList.get(position).id;  }

    public int getExerciseNamePos(int position){ return exerciseList.get(position).Pos;  }

    /** Diets **/
    public void setDietList(ArrayList<Diet> d){
        dietList = d;
    }

    public ArrayList<String> getDietList(){
        ArrayList<String> lista = new ArrayList<>();
        for(int i = 0; i < dietList.size(); ++i) {
            lista.add(dietList.get(i).name);
        }
        return lista;
    }

    public void addDiet(Diet t){
        dietList.add(t);
    }

    public void deleteDiet(String name){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                dietList.remove(i);
                break;
            }
        }
    }

    public void setDietDesc(String name, String desc){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                dietList.get(i).desc = desc;
                break;
            }
        }
    }

    public String getDietDesc(String name){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                return dietList.get(i).desc;
            }
        }
        return "";
    }

    public void setDietName(String name, String newName){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                dietList.get(i).name = newName;
                break;
            }
        }
    }

    public void setDietID(String name, int newID){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                dietList.get(i).id = newID;
                break;
            }
        }
    }

    public int getDietID(String name){
        for(int i = 0; i < dietList.size(); ++i){
            if(dietList.get(i).name.equals(name)){
                return dietList.get(i).id;
            }
        }
        return -1;
    }

    /**Dias**/
    public ArrayList<String> getDias(){
        ArrayList<String> ListaDias = new ArrayList<>();
        for(int i = 0; i < dias.length; i++){
            ListaDias.add(dias[i]);
        }
        return ListaDias;
    }

    /** Meals **/
    public ArrayList<Meal> getMealList(){
        return mealList;
    }

    public void setMealList(ArrayList<Meal> meals){
        mealList = meals;
    }

    public void addMeal(Meal m){
        mealList.add(m);
    }

    public void deleteMeal(int pos){
        mealList.remove(pos);
    }

    public void updateMeal(int pos, Meal m){
        mealList.set(pos, m);
    }

    public int sizeMealList(){return mealList.size();}

    public void setMealID(String name, int newID) {
        for(int i = 0; i < mealList.size(); ++i){
            if(mealList.get(i).name.equals(name)){
                mealList.get(i).id = newID;
                break;
            }
        }
    }

    public int getMealID(String name) {
        for (int i = 0; i < mealList.size(); ++i) {
            if (mealList.get(i).name.equals(name)) {
                return mealList.get(i).id;
            }
        }
        return -1;
    }

    /** Aliments **/
    public ArrayList<Aliment> getAlimentList(){
        return alimentList;
    }

    public void setAlimentList(ArrayList<Aliment> aliments){
        alimentList = aliments;
    }

    public void addAliment(Aliment a){
        alimentList.add(a);
    }

    public void deleteAliment(int pos){
        alimentList.remove(pos);
    }

    public void updateAliment(int pos, Aliment a){
        alimentList.set(pos, a);
    }


    /** Rutas **/
    public ArrayList<String> getRutasNames(){
        ArrayList<String> lista = new ArrayList<>();
        for(int i = 0; i < rutasList.size(); ++i) {
            lista.add(rutasList.get(i).name);
        }
        return lista;
    }

    public ArrayList<Ruta> getRutasList(){
        return rutasList;
    }

    public void setRutasList(ArrayList<Ruta> rutas){
        rutasList = rutas;
    }

    public void addRuta(Ruta r){
        rutasList.add(r);
    }

    public void deleteRuta(int pos){
        rutasList.remove(pos);
    }

    public void updateRuta(int pos, Ruta r){
        rutasList.set(pos, r);
    }

    public int getRutaID(int pos){
        return rutasList.get(pos).id;
    }

    public void setRutaID(String name, int newID){
        for(int i = 0; i < rutasList.size(); ++i){
            if(rutasList.get(i).name.equals(name)){
                rutasList.get(i).id = newID;
                break;
            }
        }
    }

    public int getAlimentID(int position){ return alimentList.get(position).id;  }

    public int getSizeAlimentList(){ return alimentList.size(); }

    public void setAlimentID(int pos, int newID){
        alimentList.get(pos).id = newID;
    }

}



class Training{
    int id;
    String name;
    String desc;
}

class Exercise{
    int id;
    String TitleEx;
    String NumRepet;
    String NumSerie;
    String NumRest;
    int  Pos;
    String Desc;
}

class Diet{
    int id;
    String name;
    String desc;
}

class Meal{
    int id;
    String name;
    String time;
}

class Aliment{
    int id;
    String name;
    String calories;
}

class Ruta{
    int id;
    String name;
    String description;
    Integer distance;
    Point origen;
    Point destino;
}

class Achievement{
    int id;
    Boolean active;
    int distance;

}

class UsersInfo{
    Integer id;
    String username;
    String description;
    String birthDate;
    String country;
    Integer nFollower;
    Integer nFollowing;
    Boolean sAge;
    Boolean sFollower;
    Boolean sMessage;
    Boolean follow;
}