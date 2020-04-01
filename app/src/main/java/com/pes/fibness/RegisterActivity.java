package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
    private boolean checkEmail = false, checkPass = false, canRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

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

        //Listener for email
        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                final String email = emailAddress.getEditableText().toString().trim();

                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                if (!email.matches(regex)) {
                    checkEmail = false;
                    emailAddress.setError("Enter a valid email.");
                }
                else checkEmail = true;
            }
        });


        //listener for password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

                if (password.getText().toString().isEmpty()) {
                    checkPass = false;
                    password.setError("Enter password");
                } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
                    checkPass = false;
                    password.setError("The password must have at least 8 characters, a lowercase, an uppercase, a number and a special character (!,@,#,&,%)");
                } else {
                    checkPass = true;
                    password.setError(null);
                }
            }
        });

        //Listener for confirm password
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (confirmPassword.getText().toString().isEmpty()) {
                    canRegister = false;
                    confirmPassword.setError("Enter confirmation password");
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    canRegister = false;
                    confirmPassword.setError("Passwords are different");
                } else {
                    if(checkEmail && checkPass) canRegister=true;
                    confirmPassword.setError(null);
                }
            }
        });




        //press register button: verification check
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "User name can't be empty.", Toast.LENGTH_LONG).show();
                else if (canRegister)   insertUser();
                else Toast.makeText(getApplicationContext(), "Error: Something went wrong. Registration Failed.", Toast.LENGTH_LONG).show();
            }
        });


    }

    //register an user in database
    private void insertUser() {
        ConnetionAPI connetion = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3000/user");
        System.out.println("Hola");
        connetion.postUser(userName.getText().toString(), password.getText().toString(), emailAddress.getText().toString());
    }

    //press back to back mainpage
    @Override
    public void onBackPressed() {
        Intent homePage = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(homePage);
    }




}


