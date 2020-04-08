package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pes.fibness.ui.perfil.EditProfileActivity;

import static android.view.View.*;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*press back button to back Fragment_perfil*/
        ImageView backButton = (ImageView) findViewById(R.id.backImgButton);
        backButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);

            }
        });


        /*press log out to close session*/
        TextView logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        /*press to delete account*/












    }




}
