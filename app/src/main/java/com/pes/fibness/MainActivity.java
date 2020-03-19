package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private TextView forgotPassword;
    private TextView fbLogin;
    private TextView newAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        fbLogin = (TextView) findViewById(R.id.fb_login);
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
                String user = userName.getText().toString();
                String pwd = password.getText().toString();
                Boolean check = true; //get password from DB and check

                if(check) {
                    Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homePage);
                }
                else
                    System.out.println("Error");


            }
        });



    }

}
