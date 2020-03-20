package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    }



}
