package com.pes.fibness;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sun.mail.iap.ByteArray;
import com.mapbox.geojson.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConnetionAPI {

    private RequestQueue requestQueue;
    private String urlAPI;
    private StringRequest request;
    private Context context;


    public ConnetionAPI(Context context, String url){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.urlAPI = url;
    }

    /*test to sure that database work*/
    public void getTest(){
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("INFO: "+ response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("INFO: ERROR");
            }
        });
        enqueue();
    }

    /*to register an user in database*/
    public void postUser(String userName, String password, String emailAddress){

        String securePassword = Password.hashPassword(password); //hashed password
        System.out.println("hashed password: "+ securePassword);
        //JSON data in  string format
        final String data = "{"+
                "\"nombre\": " + "\"" + userName + "\"," +
                "\"password\": " + "\"" + securePassword + "\"," +
                "\"email\": " + "\"" + emailAddress+ "\"" +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Resgister user response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("-----------------------------------");
                    System.out.println("result: " + obj.get("created"));
                    Boolean b = (Boolean) obj.get("created");
                    if(b){
                        User u = User.getInstance();
                        Integer id = (Integer) obj.get("id");
                        u.setId(id);
                        System.out.println("user_id: "+ u.getId());
                        /*nedd to load user information & setting*/
                        getUserInfo("http://10.4.41.146:3001/user/"+id+"/info");
                        getUserSettings("http://10.4.41.146:3001/user/"+id+"/settings");
                        homeActivity();
                    }
                    else Toast.makeText(getApplicationContext(), "Register response error", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }


        };

        enqueue();

    }


    /*Facebook user*/
    public void fbUser(String name, String email, String id) {

        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"email\": " + "\"" + email + "\"," +
                "\"password\": " + "\"" + id + "\"" +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("-----------------------------------");
                    System.out.println("result: " + obj.get("id"));

                    User u = User.getInstance();
                    Integer id = (Integer) obj.get("id");
                    u.setId(id);

                    getUserInfo("http://10.4.41.146:3001/user/"+id+"/info");
                    getUserSettings("http://10.4.41.146:3001/user/"+id+"/settings");
                    homeActivity();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

            }

        };
        enqueue();


    }



    /*validate user entry*/
    public void validateUser(String emailAddress, String password) {

        String securePassword = Password.hashPassword(password);
        //JSON data in  string format
        final String data = "{"+
                "\"email\": " + "\"" + emailAddress + "\"," +
                "\"password\": " + "\"" + securePassword + "\"" +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("-----------------------------------");
                    System.out.println("result: " + obj.get("result"));
                    Boolean b = (Boolean) obj.get("result");
                    if(b){
                        User u = User.getInstance();
                        Integer id = (Integer) obj.get("id");
                        u.setId(id);
                        System.out.println("user_id: "+ u.getId());
                        /*nedd to load user information & setting*/
                        getUserInfo("http://10.4.41.146:3001/user/"+id+"/info");
                        getUserSettings("http://10.4.41.146:3001/user/"+id+"/settings");

                        homeActivity();
                    }
                    else Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

            }

        };
        enqueue();

    }


    public void deleteUser(){

        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                if(response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your account has been deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                else Toast.makeText(getApplicationContext(), "Your account has not been deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });

        enqueue();



    }



    public void resetPassword(String emailAddress, String newPassword) {
        System.out.println("Dentro de la resetear contraseña");
        String securePassword = Password.hashPassword(newPassword);

        final String data = "{"+
                "\"email\": " + "\"" + emailAddress + "\"," +
                "\"password\": " + "\"" + securePassword + "\"" +
                "}";

        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                if(response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your password has been reset successfully", Toast.LENGTH_SHORT).show();
                    homeActivity();
                }
                else Toast.makeText(getApplicationContext(), "Your password has not been reset successfully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

            }

        };
        enqueue();



    }



    /*para settings*/
    public void getUserSettings(String route){
        System.out.println("Dentro de User settings");

        request = new StringRequest(Request.Method.GET, route, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("-----------------------------------");
                    /*
                    Boolean b1 = (Boolean) obj.get("sedad");
                    System.out.println("b1: " + obj.get("sedad") + " b2: " + obj.get("sdistancia") + " b3: " + obj.get("sinvitacion") + " b4: " + obj.get("sseguidor") + " b5: " + obj.get("nmensaje"));
                    */
                    boolean[] s = {(boolean) obj.get("sedad"), (boolean) obj.get("sdistancia"), (boolean) obj.get("sinvitacion"), (boolean) obj.get("sseguidor"), (boolean) obj.get("nmensaje")};
                    User.getInstance().setSettings(s);
                    System.out.println("my setting: " + User.getInstance().getSettings());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });

        enqueue();

    }


    public void postUserSettings(boolean[] settings){

        System.out.println("Dentro de la post settings");


        final String data = "{"+
                "\"sEdad\": " + settings[0] +"," +
                "\"sDistancia\": "  + settings[1] + "," +
                "\"sInvitacion\": "  + settings[2] + "," +
                "\"sSeguidor\": " + settings[3] +"," +
                "\"nMensaje\": " + settings[4] +
                "}";

        System.out.println("resultados: " + data );

        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta post user: "+ response);
                if(response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Changes made", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "Changes did not make", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

            }

        };
        enqueue();





    }

    private void getUserProfile(String s) {
        System.out.println("--------------------------------------------------------------------");
        request = new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // System.out.println("Respuesta imagen: " + response);
                byte[] pic = Base64.decode(response, Base64.DEFAULT);
              //  System.out.println("Respuesta imagen: " + Arrays.toString(pic));
                User u = User.getInstance();
                u.setImage(pic);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });

        enqueue();
    }

    private void getUserInfo(String route) {

        final User u = User.getInstance();

        System.out.println("Dentro de User information");

        getUserProfile("http://10.4.41.146:3001/user/" + u.getId() + "/profile");

        request = new StringRequest(Request.Method.GET, route, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("Estoy dentro de User info para recoger datos de json");
                    System.out.println("Resultado: " + obj);

                    User u = User.getInstance();

                    u.setName(obj.get("nombre").toString());
                    u.setEmail(obj.get("email").toString());
                    u.setImageRoute(obj.get("rutaimagen").toString());
                    u.setDescription(obj.get("descripcion").toString());
                    u.setBirthDate(obj.get("fechadenacimiento").toString());
                    u.setRegisterDate(obj.get("fechaderegistro").toString());
                    u.setUserType(obj.get("tipousuario").toString());
                    u.setProfileType(obj.get("tipoperfil").toString());
                    u.setCountry(obj.get("pais").toString());
                    u.setGender(obj.get("genero").toString());
                    u.setnFollower((Integer) obj.get("nseguidores"));
                    u.setnFollowing((Integer) obj.get("nseguidos"));
                    u.setnPost((Integer) obj.get("npost"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });

        enqueue();




    }


    /*para trainings*/
    public void getTrainingExercises(final String title){
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray exercises = new JSONArray(response);
                    ArrayList<Exercise> exerciseList = new ArrayList<>();
                    for(int i = 0; i < exercises.length(); i++){
                        JSONObject exercise = exercises.getJSONObject(i);
                        Exercise e = new Exercise();
                        e.TitleEx = (String) exercise.getString("nombre");
                        e.Pos = (Integer) exercise.getInt("posicion");
                        int numRest = (Integer) exercise.getInt("tiempodescanso");
                        e.NumRest = String.valueOf(numRest);
                        int numSerie = (Integer) exercise.getInt("numsets");
                        e.NumSerie = String.valueOf(numSerie);
                        int numRept = (Integer) exercise.getInt("numrepeticiones");
                        e.NumRepet = String.valueOf(numRept);
                        e.id = (Integer) exercise.getInt("idactividad");
                        exerciseList.add(e);
                    }
                    User.getInstance().setExerciseList(exerciseList);
                    Intent TrainingPage = new Intent(context, CreateTrainingActivity.class);
                    TrainingPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    TrainingPage.putExtra("new", false);
                    TrainingPage.putExtra("title", title);
                    context.startActivity(TrainingPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HOLA" + error);
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();

    }

    public void postTrainingExercises(int idT, final String nameE, final int pos, final int numRest, final int numSerie, final int numRept,
                                      final int Position){
        final String data = "{"+
                "\"idEntrenamiento\": " + idT +"," +
                "\"nombre\": " + "\"" + nameE + "\"," +
                "\"descripcion\": " + "\"" + "" + "\"," +
                "\"tiempoEjecucion\": " + 0 + "," +
                "\"numSets\": " + numSerie + "," +
                "\"numRepeticiones\": " + numRept + "," +
                "\"tiempoDescanso\": " + numRest + "," +
                "\"posicion\": " + pos +
                "}";


        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Resgister user response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idExercise")) {
                        int id = (Integer) obj.get("idExercise");
                        Exercise e = new Exercise();
                        e.TitleEx = nameE;
                        e.NumSerie = String.valueOf(numSerie);
                        e.NumRest = String.valueOf(numRest);
                        e.NumRepet = String.valueOf(numRept);
                        e.Pos = pos;
                        e.id = id;
                        User.getInstance().updateExercise(Position, e);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HIII " + error);
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }


        };

        enqueue();
    }

    public void updateTrainingExercises(final String nameE, int pos, final int numRest, final int numSerie, final int numRept){
        final String data = "{"+
                "\"nombre\": " + "\"" + nameE + "\"," +
                "\"descripcion\": " + "\"" + "" + "\"," +
                "\"tiempoEjecucion\": " + 0 + "," +
                "\"numSets\": " + numSerie + "," +
                "\"numRepeticiones\": " + numRept + "," +
                "\"tiempoDescanso\": " + numRest + "," +
                "\"posicion\": " + pos +
                "}";
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("NANIIII "  + response);
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your exercise has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HIII " + error);
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }


        };

        enqueue();

    }

    public void deleteTrainingExercises(){
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your exercise has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HIII " + error);
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();

    }

    public void getUserTrainings(){

        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray trainings = new JSONArray(response);
                    ArrayList<Training> trainingList = new ArrayList<>();
                    for(int i = 0; i < trainings.length(); i++){
                        JSONObject training = trainings.getJSONObject(i);
                        Training t = new Training();
                        t.name = (String) training.getString("nombre");
                        t.desc = (String) training.getString("descripcion");
                        t.id = (Integer) training.getInt("idelemento");
                        trainingList.add(t);
                    }
                    User.getInstance().setTrainingList(trainingList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HOLA" + error);
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }

    public void postUserTraining(int userID, String name, String desc){
        final String nombre = name;
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + desc + "\"," +
                "\"idUser\": " + userID +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idElemento")) {
                        int id = (Integer) obj.get("idElemento");
                        User.getInstance().setTrainingID(nombre, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();
    }

    public void updateUserTraining(String name, String desc){
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + desc + "\"" +
                "}";
        System.out.println("HOLA " + name + " " + desc);
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your training has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }


        };

        enqueue();

    }

    public void deleteUserTraining(){
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your training has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }


    public void setUserProfilePicture() {
        System.out.println("--------------------------------------------------------------------");

        User u = User.getInstance();
        byte[] pic = u.getImage();
        final String data = Base64.encodeToString(pic, Base64.DEFAULT);

        //System.out.println("post response image: " + data);

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    System.out.println("post response: " + response);
                    Toast.makeText(getApplicationContext(), "Your information has been modified", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Server Response error", Toast.LENGTH_LONG).show();
                }
            }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "image/jpeg; charset=utf-8";
            }
            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
            };

        enqueue();
    }


    public void postUserInfo() {

        User u = User.getInstance();
        final String data = "{"+
                "\"nombre\": " + "\"" + u.getName() + "\"," +
                "\"genero\": " + "\"" + u.getGender() + "\"," +
                "\"descripcion\": " + "\"" + u.getDescription() + "\"," +
                "\"fechadenacimiento\": " + "\"" + u.getBirthDate() + "\"," +
                "\"pais\": " + "\"" + u.getCountry() + "\"" +
                "}";
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("post response: " + response);
                Toast.makeText(getApplicationContext(), "Your information has been modified", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Server Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();
    }

    /*para dietas*/
    public void getUserDiets(){
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray diets = new JSONArray(response);
                    ArrayList<Diet> dietList = new ArrayList<>();
                    for(int i = 0; i < diets.length(); i++){
                        JSONObject diet = diets.getJSONObject(i);
                        Diet d = new Diet();
                        d.name = (String) diet.getString("nombre");
                        d.desc = (String) diet.getString("descripcion");
                        d.id = (Integer) diet.getInt("idelemento");
                        dietList.add(d);
                    }
                    User.getInstance().setDietList(dietList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HOLA" + error);
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }

    public void postUserDiets(int userID, String name, String desc){
        final String nombre = name;
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + desc + "\"," +
                "\"idUser\": " + userID +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idElemento")) {
                        int id = (Integer) obj.get("idElemento");
                        User.getInstance().setDietID(nombre, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();

    }

    public void updateUserDiets(String name, String desc){
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + desc + "\"" +
                "}";
        System.out.println("HOLA " + name + " " + desc);
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your diet has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }


        };

        enqueue();
    }

    public void deleteUserDiets(){
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your diet has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();

    }

    public void getDietMeal(final String title, final String dia){
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray meals = new JSONArray(response);
                    ArrayList<Meal> mealList = new ArrayList<>();
                    for(int i = 0; i < meals.length(); i++){
                        JSONObject meal = meals.getJSONObject(i);
                        Meal m = new Meal();
                        m.id = (Integer) meal.getInt("idcomida");
                        m.name = (String) meal.getString("nombre");
                        m.time = (String) meal.getString("horacomida");
                        mealList.add(m);
                    }
                    User.getInstance().setMealList(mealList);
                    Intent MealPage = new Intent(context, MealActivity.class);
                    MealPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MealPage.putExtra("dia", dia);
                    MealPage.putExtra("title", title);
                    context.startActivity(MealPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HOLA" + error);
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }

    public void postDietMeal(int idDiet, String name, String time, String day){
        final String nombre = name;
        System.out.println(time);
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"horaComida\": " + "\"" + time + "\"," +
                "\"idElemento\": " + idDiet + "," +
                "\"tipoDia\": " + "\"" + day + "\"" +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idcomida")) {
                        int id = (Integer) obj.get("idcomida");
                        User.getInstance().setMealID(nombre, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR " + error);
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();

    }

    public void updateDietMeal(String name, String time){
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"horaComida\": " + "\"" + time + "\"" +
                "}";
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your meal has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }


        };

        enqueue();

    }

    public void deleteDietMeal(){
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your meal has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();

    }

    public void getMealAliment(final String dieta, final String dia, final String comida, final boolean New){
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray food = new JSONArray(response);
                    ArrayList<Aliment> foodList = new ArrayList<>();
                    for(int i = 0; i < food.length(); i++){
                        JSONObject aliment = food.getJSONObject(i);
                        Aliment a = new Aliment();
                        a.id = (Integer) aliment.getInt("idalimento");
                        a.name = (String) aliment.getString("nombre");
                        int cal = (Integer) aliment.getInt("calorias");
                        a.calories = String.valueOf(cal);
                        foodList.add(a);
                    }
                    User.getInstance().setAlimentList(foodList);
                    Intent FoodPage = new Intent(context, CreateDietActivity.class);
                    FoodPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    FoodPage.putExtra("dia", dia);
                    FoodPage.putExtra("title", dieta);
                    FoodPage.putExtra("comida", comida);
                    FoodPage.putExtra("new", New);
                    context.startActivity(FoodPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HOLA" + error);
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }

    public void postMealAliment(int idMeal, String name, String calories, final int pos){
        final String nombre = name;
        int cal = Integer.parseInt(calories);
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + "" + "\"," +
                "\"calorias\": " +  cal + "," +
                "\"idComida\": " + idMeal +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idalimento")) {
                        int id = (Integer) obj.get("idalimento");
                        User.getInstance().setAlimentID(pos, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();

    }

    public void updateMealAliment(String name, String calories){
        int cal = Integer.parseInt(calories);
        final String data = "{"+
                "\"nombre\": " + "\"" + name + "\"," +
                "\"descripcion\": " + "\"" + "" + "\"," +
                "\"calorias\": " + cal +
                "}";
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your aliment has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }


        };

        enqueue();

    }

    public void deleteMealAliment(){
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your aliment has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();

    }


    /** RUTAS **/
    public void getUserRoutes() {
        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray rutas = new JSONArray(response);
                    ArrayList<Ruta> rutasList = new ArrayList<>();
                    for(int i = 0; i < rutas.length(); i++){
                        JSONObject ruta = rutas.getJSONObject(i);
                        String[] origen;
                        String[] destino;
                        Ruta r = new Ruta();
                        r.name = (String) ruta.getString("nombre");
                        r.description = (String) ruta.getString("descripcion");
                        r.id = (Integer) ruta.getInt("idelemento");
                        r.distance = Integer.parseInt(ruta.getString("distancia"));
                        origen = ruta.getString("origen").split(";");
                        destino = ruta.getString("destino").split(";");
                        r.origen = Point.fromLngLat(Double.parseDouble(origen[0]), Double.parseDouble(origen[1]));
                        r.destino = Point.fromLngLat(Double.parseDouble(destino[0]), Double.parseDouble(destino[1]));
                        rutasList.add(r);
                    }
                    User.getInstance().setRutasList(rutasList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }

    public void updateUserRoute(Ruta r) {
        String origen = r.origen.longitude() +";"+ r.origen.latitude();
        String destino = r.destino.longitude() +";"+ r.destino.latitude();
        final String data = "{"+
                "\"nombre\": " + "\"" + r.name + "\"," +
                "\"descripcion\": " + "\"" + r.description + "\"," +
                "\"origen\": " + "\"" + origen + "\"," +
                "\"destino\": " + "\"" + destino + "\"," +
                "\"distancia\": " + "\"" + r.distance + "\"" +
                "}";
        request = new StringRequest(Request.Method.PUT, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your route has not been modified. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }


        };

        enqueue();
    }

    public void postUserRoute(Ruta r, int userID) {
        String origen = r.origen.longitude() +";"+ r.origen.latitude();
        String destino = r.destino.longitude() +";"+ r.destino.latitude();
        final String name = r.name;
        final String data = "{"+
                "\"nombre\": " + "\"" + r.name + "\"," +
                "\"descripcion\": " + "\"" + r.description + "\"," +
                "\"idUser\": " + userID + "," +
                "\"origen\": " + "\"" + origen + "\"," +
                "\"destino\": " + "\"" + destino + "\"," +
                "\"distancia\": " + "\"" + r.distance + "\"" +
                "}";

        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.has("idElemento")) {
                        int id = (Integer) obj.getInt("idElemento");
                        User.getInstance().setRutaID(name, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return data.getBytes(StandardCharsets.UTF_8);
            }
        };
        enqueue();
    }

    public void deleteUserRoute() {
        request = new StringRequest(Request.Method.DELETE, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("OK")){
                    Toast.makeText(getApplicationContext(), "Your route has not been deleted. Re-open application and try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response error", Toast.LENGTH_LONG).show();
            }
        });
        enqueue();
    }



    //to go HomePage
    private void homeActivity() {
        Intent homePage = new Intent(context, HomeActivity.class);
        homePage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(homePage);
    }


    private void enqueue(){
        requestQueue.add(request);
    }



}