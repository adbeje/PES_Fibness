package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MapActivity extends AppCompatActivity {

    private String origen = "";
    private String destino = "";
    private String tituloRuta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_rutas);
        getExtras();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        origen = extras.getString("origen");
        destino = extras.getString("destino");
        tituloRuta = extras.getString("tituloRuta");
    }

}
