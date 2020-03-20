package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private TextView backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";;

        userName = (EditText) findViewById(R.id.username);
        emailAddress = (EditText) findViewById(R.id.email_address);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        register = (Button) findViewById(R.id.btn_register);
        backLogin = (TextView) findViewById(R.id.back_login);



        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }
        });


        //PASSWORD
        password.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)  {
                final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

                if (password.getText().toString().isEmpty()) {
                    password.setError("Enter password");
                } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
                    password.setError("La contraseña ha de tener como mínimo 8 caracteres, una minúscula, una mayúscula, un número y un carácter especial (!,@,#,&,%)");
                } else {
                    password.setError(null);
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (confirmPassword.getText().toString().isEmpty()) {
                    confirmPassword.setError("Enter confirmation password");
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPassword.setError("Passwords are different");
                } else {
                    confirmPassword.setError(null);
                }
            }
        });




        //REGISTER TO USER
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertUser();
            }
        });

    }

    /*
    private void insertUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String urlPost = "http://10.4.41.146:3000/users"; //api link
        StringRequest request = new StringRequest(Request.Method.POST, urlPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Post data: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Post data: ERROR");
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("nombre",  userName.getText().toString());
                params.put("password",  password.getText().toString());
                params.put("email", emailAddress.getText().toString());
                return params;

            }
            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return  params;
            }

             */

    //};
        //requestQueue.add(request);


        /*
        //JSON data in  string format
        String data = "{"+
                "\"nombre\"" + "\"" + userName.getText().toString() + "\"," +
                "\"password\"" + "\"" + password.getText().toString() + "\"," +
                "\"email\"" + "\"" + emailAddress.getText().toString() + "\"" +
                "}";


        StringRequest request = new StringRequest(Request.Method.POST, urlPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //get response on JSONObject
                try {
                    JSONObject objRespense = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), objRespense.toString(), Toast.LENGTH_LONG).show(); //print response
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage() + " ErrorListener", Toast.LENGTH_SHORT).show();
            }
        })  {
            //post data to server


        };

        requestQueue.add(request);
         */


    //}
    //*/


}
