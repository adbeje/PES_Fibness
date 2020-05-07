package com.pes.fibness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
                try {
                    JSONObject obj = new JSONObject(response);
                    Boolean b = (Boolean)obj.get("result");
                    if(b){
                        Toast.makeText(getApplicationContext(), "Your password has been reset successfully", Toast.LENGTH_SHORT).show();
                        homeActivity();
                    }
                    else Toast.makeText(getApplicationContext(), "Your password has not been reset successfully", Toast.LENGTH_SHORT).show();


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

    private void getUserInfo(String route) {

        System.out.println("Dentro de User information");

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
    public void getTrainingExercises(){
        //cuando este la ruta de api definida se añadira el codigo
    }

    public void postTrainingExercises(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void updateTrainingExercises(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void deleteTrainingExercises(){
        //cuando este la ruta de api definida se añadira el codigo

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
        //cuando este la ruta de api definida se añadira el codigo
    }
    public void postUserDiets(){
        //cuando este la ruta de api definida se añadira el codigo

    }
    public void updateUserDiets(){
        //cuando este la ruta de api definida se añadira el codigo
    }
    public void deleteUserDiets(){
        //cuando este la ruta de api definida se añadira el codigo

    }
    public void getDietMeal(){
        //cuando este la ruta de api definida se añadira el codigo
    }

    public void postDietMeal(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void updateDietMeal(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void deleteDietMeal(){
        //cuando este la ruta de api definida se añadira el codigo

    }
    public void getMealAliment(){
        //cuando este la ruta de api definida se añadira el codigo
    }

    public void postMealAliment(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void updateMealAliment(){
        //cuando este la ruta de api definida se añadira el codigo

    }

    public void deleteMealAliment(){
        //cuando este la ruta de api definida se añadira el codigo

    }


    /*load id & username to show in SearchUsers activity*/
    public void getShortUserInfo(Integer id) {
        System.out.println("Dentro de getShortUserInfo");

        request = new StringRequest(Request.Method.GET, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Respuesta: "+ response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Pair<Integer,String>> users = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            users.add(i, new Pair<Integer, String>((Integer) obj.get("id"), (String) obj.get("nombre")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    User.getInstance().setShortUsersInfo(users);

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