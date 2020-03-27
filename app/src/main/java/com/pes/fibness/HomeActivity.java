package com.pes.fibness;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.pes.fibness.ui.dietas.DietasFragment;
import com.pes.fibness.ui.entrenamientos.EntrenamientosFragment;
import com.pes.fibness.ui.eventos.EventosFragment;
import com.pes.fibness.ui.perfil.PerfilFragment;
import com.pes.fibness.ui.rutas.RutasFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends AppCompatActivity {

    Fragment perfil = new PerfilFragment();
    Fragment entrenamientos = new EntrenamientosFragment();
    Fragment rutas = new RutasFragment();
    Fragment dietas = new DietasFragment();
    Fragment eventos = new EventosFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
