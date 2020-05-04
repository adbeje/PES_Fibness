package com.pes.fibness;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends AppCompatActivity {

    Fragment perfil = new ProfileFragment();
    Fragment entrenamientos = new EntrenamientosFragment();
    Fragment rutas = new RutasFragment();
    Fragment dietas = new DietasFragment();
    Fragment eventos = new EventosFragment();
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ConnetionAPI c = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/" + User.getInstance().getId() + "/trainings");
        c.getUserTrainings();
        ConnetionAPI d = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/" + User.getInstance().getId() + "/diets");
        d.getUserDiets();
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

    //press back to back mainpage
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000 > System.currentTimeMillis()){ //compare first click time with second
            Intent homePage = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(homePage);;
        }
        else Toast.makeText( getBaseContext(), "Press back again", Toast.LENGTH_SHORT).show();

        backPressedTime = System.currentTimeMillis();

    }


}
