package com.pes.fibness;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

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
                        User u = User.getInstances();
                        Integer id = (Integer) obj.get("id");
                        u.setId(id);
                        System.out.println("user_id: "+ u.getId());
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


    //validate user entry
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
                        User u = User.getInstances();
                        Integer id = (Integer) obj.get("id");
                        u.setId(id);
                        System.out.println("user_id: "+ u.getId());
                        /*nedd to load user information & setting*/
                        //getUserInfo("http://10.4.41.146:3001/user/+id+/userInfo");
                        getUserSettings("http://10.4.41.146:3001/user/"+id+"/userSettings");

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
                    System.out.println("b1: " + obj.get("sedad"));
                    System.out.println("b2: " + obj.get("sdistancia"));
                    System.out.println("b3: " + obj.get("sinvitacion"));
                    System.out.println("b4: " + obj.get("sseguidor"));
                    System.out.println("b5: " + obj.get("nmensaje"));
                     */
                    boolean[] s = {(boolean) obj.get("sedad"), (boolean) obj.get("sdistancia"), (boolean) obj.get("sinvitacion"), (boolean) obj.get("sseguidor"), (boolean) obj.get("nmensaje")};
                    User.getInstances().setSettings(s);
                    System.out.println("my setting: " + User.getInstances().getSettings());

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
                "\"sEdad\": " + "\"" + settings[0] + "\"," +
                "\"sDistancia\": " + "\"" + settings[1] + "\"" +
                "\"sInvitacion\": " + "\"" + settings[2] + "\"," +
                "\"sSeguidor\": " + "\"" + settings[3] + "\"," +
                "\"nMensaje\": " + "\"" + settings[4] + "\"," +
                "}";

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