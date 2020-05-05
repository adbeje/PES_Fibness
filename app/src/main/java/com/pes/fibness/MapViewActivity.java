package com.pes.fibness;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class MapViewActivity extends AppCompatActivity {

    private String origen = "";
    private String destino = "";
    private String tituloRuta = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ruta);
        getExtras();

    }


    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        origen = extras.getString("origen");
        destino = extras.getString("destino");
        tituloRuta = extras.getString("tituloRuta");
    }

}
