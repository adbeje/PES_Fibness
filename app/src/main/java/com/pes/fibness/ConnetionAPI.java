package com.pes.fibness;

import android.app.Activity;

import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ConnetionAPI {

    private RequestQueue requestQueue;
    private String urlAPI;
    private StringRequest request;
    private Activity activity;


    public ConnetionAPI(Activity activity, String url){
        this.activity = activity;
        this.requestQueue = Volley.newRequestQueue(activity);
        this.urlAPI = url;
    }

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

    public void postUser(String userName, String password, String emailAddress){
        /*
        String securePassword = Password.hashPassword(password);
        System.out.println("hashed password: " + securePassword);
        System.out.println("resultado: " + Password.checkPassword(password, securePassword));
        //JSON data in  string format
        final String data = "{" +
                "\"nombre\": " + "\"" + userName + "\"," +
                "\"password\": " + "\"" + securePassword + "\"," +
                "\"email\": " + "\"" + emailAddress + "\"" +
                "}";
        request = new StringRequest(Request.Method.POST, this.urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("OK")) {
                    //we have to load the meni with user information, in this case we have to set the info that we have
                    homeActivity(); //if all has gone good, go homePage
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error+"", Toast.LENGTH_SHORT).show();
                //System.out.println("Post data: ERROR " + error);
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //System.out.println(data);
                    return data == null ? null : data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    //e.printStackTrace();
                    return null;
                }
            }
        };
        enqueue();
       */

    }

    private void homeActivity() {
        Intent homePage = new Intent(activity, HomeActivity.class);
        activity.startActivity(homePage);
    }


    private void enqueue(){
        requestQueue.add(request);
    }


}