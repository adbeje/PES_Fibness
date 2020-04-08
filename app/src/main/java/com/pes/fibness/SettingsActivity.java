package com.pes.fibness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


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
                startActivity(intent);

            }
        });


        /*press log out to close session*/
        TextView logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*press to delete account*/
        TextView delete = findViewById(R.id.deleteAccount);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningMessage();
            }
        });




    }

    private void showWarningMessage() {
        AlertDialog.Builder message = new AlertDialog.Builder(this);

        message.setMessage("Are you sure you want to delete your account?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("entro para borrar");
                        User u = User.getInstances();
                        ConnetionAPI connetion = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3000/user/"+u.getId());
                        connetion.deleteUser();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = message.create();
        alertDialog.show();


    }


}
