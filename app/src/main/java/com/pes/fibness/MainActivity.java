package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private EditText emailAddress;
    private EditText password;
    private Button login;
    private LoginButton loginFb;
    private TextView forgotPassword;
    private TextView newAccount;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        loginFb = (LoginButton) findViewById(R.id.login_button);
        loginFb.setReadPermissions("email");
        emailAddress = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        newAccount = (TextView) findViewById(R.id.new_account);

        //press to register a new account
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);

            }
        });


        //press to login (an example)
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String compruebaEmail = emailAddress.getEditableText().toString().trim();
                Boolean check = true; //get password from DB and check
                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                if (!compruebaEmail.matches(regex))
                {
                    emailAddress.setError("Enter a valid email.");
                    check = false;
                }


                String user = emailAddress.getText().toString();
                String pwd = password.getText().toString();

                /*
                //PRUEBA CONEXION CON BD EXISTO
                //send get request
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                final String urlApi = "http://10.4.41.146:3000/test";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlApi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("INfO: "+ response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("INfO: ERROR");
                    }
                });
                requestQueue.add(stringRequest);
                */

                if(check) {
                    homeActivity();
                }
                else
                    System.out.println("Error");


            }
        });

        // Callback registration
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                homeActivity();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void homeActivity() {
        Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homePage);
    }


    private void goHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
