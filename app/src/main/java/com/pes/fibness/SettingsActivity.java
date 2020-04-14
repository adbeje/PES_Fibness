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
import android.widget.Switch;
import android.widget.TextView;


import static android.view.View.*;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchAge, switchDistance, switchInvitation, switchLike, switchFollower,switchMessage;
    private TextView textContact, logOut, done, changeLanguage, delete;
    private ImageView backButton;
    private boolean switchOn1, switchOn2, switchOn3, switchOn4, switchOn5, switchOn6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*press back button to back Fragment_perfil*/
        backButton = (ImageView) findViewById(R.id.backImgButton);
        switchAge = findViewById(R.id.switchAge);
        switchDistance = findViewById(R.id.switchDistance);
        switchInvitation = findViewById(R.id.switchInvitation);
        switchLike = findViewById(R.id.switchLike);
        switchFollower = findViewById(R.id.switchFollower);
        switchMessage = findViewById(R.id.switchMessage);
        textContact = findViewById(R.id.textContact);
        done = findViewById(R.id.done);
        logOut = findViewById(R.id.logOut);
        changeLanguage = findViewById(R.id.changeLanguage);
        delete = findViewById(R.id.deleteAccount);

        /*to go back*/
        backButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        /*save settings data*/
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        /*change language*/
        changeLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //showChangeLanguageDialog();
            }
        });


        /*press log out to close session*/
        logOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*press to delete account*/
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningMessage();
            }
        });


    }

    private void saveData() {
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
                        ConnetionAPI connetion = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/"+u.getId());
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
