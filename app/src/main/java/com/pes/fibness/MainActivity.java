package com.pes.fibness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import javax.net.ssl.HandshakeCompletedEvent;

public class MainActivity extends AppCompatActivity {

    Fragment perfil = new ProfileFragment();
    Fragment entrenamientos = new EntrenamientosFragment();
    Fragment rutas = new RoutesFragment();
    Fragment dietas = new DietasFragment();
    Fragment eventos = new EventosFragment();
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSharedPreferences();

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        @SuppressLint("HandlerLeak") Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                setContentView(R.layout.activity_home);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, perfil);
                ft.commit();
                BottomBar bottomBar = findViewById(R.id.bottomBar);
                bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelected(@IdRes int tabId) {
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        if(tabId == R.id.tab_perfil) {
                            ft2.replace(R.id.fragment_layout, perfil);
                        }
                        else if(tabId == R.id.tab_entrenamientos) {
                            ft2.replace(R.id.fragment_layout, entrenamientos);
                        }
                        else if(tabId == R.id.tab_rutas) {
                            ft2.replace(R.id.fragment_layout, rutas);
                        }
                        else if(tabId == R.id.tab_dietas) {
                            ft2.replace(R.id.fragment_layout, dietas);
                        }
                        else if(tabId == R.id.tab_eventos) {
                            ft2.replace(R.id.fragment_layout, eventos);
                        }
                        ft2.commit();
                    }
                });
            }
        };
        h.sendEmptyMessageDelayed(0, 500);
    }

    private void getSharedPreferences(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

        String userEmail = preferences.getString("userEmail", "");
        String userPassword = preferences.getString("userPassword", "");

        if (userEmail == "" && userPassword == "") {
            finish();
            Intent logIn = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logIn);
        }
        else {
            checkUser(userEmail, userPassword);
        }

    }

    private void checkUser(String userEmail, String userPassword) {

        ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/validate");
        connection.validateUser(userEmail, userPassword);

        @SuppressLint("HandlerLeak") Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ConnetionAPI connection;

                connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/" + User.getInstance().getId() + "/trainings");
                connection.getUserTrainings();

                connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/" + User.getInstance().getId() + "/routes");
                connection.getUserRoutes();

                connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/" + User.getInstance().getId() + "/diets");
                connection.getUserDiets();
            }
        };
        h.sendEmptyMessageDelayed(0, 500);

    }

    //press back to back mainpage
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000 > System.currentTimeMillis()){ //compare first click time with second
            finish();
        }
        else Toast.makeText( getBaseContext(), "Press back again", Toast.LENGTH_SHORT).show();

        backPressedTime = System.currentTimeMillis();

    }


}
