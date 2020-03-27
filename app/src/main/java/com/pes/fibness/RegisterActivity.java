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
    private boolean checkPass = false;
    private boolean canRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
        ;

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String compruebaEmail = emailAddress.getEditableText().toString().trim();

                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                if (!compruebaEmail.matches(regex)) {
                    emailAddress.setError("Enter a valid email.");
                }
            }
        });


        //PASSWORD
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

                if (password.getText().toString().isEmpty()) {
                    checkPass = false;
                    password.setError("Enter password");
                } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
                    checkPass = false;
                    password.setError("La contraseña ha de tener como mínimo 8 caracteres, una minúscula, una mayúscula, un número y un carácter especial (!,@,#,&,%)");
                } else {
                    checkPass = true;
                    password.setError(null);
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (confirmPassword.getText().toString().isEmpty()) {
                    canRegister = false;
                    confirmPassword.setError("Enter confirmation password");
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    canRegister = false;
                    confirmPassword.setError("Passwords are different");
                } else {
                    if(checkPass) canRegister=true;
                    confirmPassword.setError(null);
                }
            }
        });


        //REGISTER TO USER
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userName.getText().toString().isEmpty() && checkEmail(emailAddress.getText().toString()) && checkPass && canRegister){
                    insertUser();
                }
                else Toast.makeText(getApplicationContext(), "Error: Something went wrong. User Registration Failed.", Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void insertUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String urlPost = "http://10.4.41.146:3000/user"; //api link

        //hashed password
        String securePassword = Password.hashPassword(password.getText().toString());
        System.out.println("hashed password: "+ securePassword);

        //JSON data in  string format
        final String data = "{"+
                "\"nombre\": " + "\"" + userName.getText().toString() + "\"," +
                "\"password\": " + "\"" + securePassword + "\"," +
                "\"email\": " + "\"" + emailAddress.getText().toString() + "\"" +
                "}";

        StringRequest request = new StringRequest(Request.Method.POST, urlPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("OK"))
                    homeActivity();
                else Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: Something went wrong. User Registration Failed.", Toast.LENGTH_LONG).show();
            }
        }) {
            //post data to server
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    //System.out.println(data);
                    return data == null ? null: data.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    //e.printStackTrace();
                    return null;
                }

            }


        };
        requestQueue.add(request);
    }
    private void homeActivity() {
        Intent homePage = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(homePage);
    }

}


