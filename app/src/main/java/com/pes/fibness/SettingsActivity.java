package com.pes.fibness;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import static android.view.View.*;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchAge, switchDistance, switchInvitation, switchLike, switchFollower,switchMessage;
    private TextView textContact, logOut, done, changeLanguage, delete;
    private ImageView backButton;
    private boolean switchOn1, switchOn2, switchOn3, switchOn4, switchOn5, switchOn6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
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
                saveSettingsData();
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        textContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });



        /*change language*/
        changeLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
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

        /*
        loadData();
        updateView();
        */


    }



    /*send email to fibness*/
    private void sendMail() {
        String fibnessEmail = "fibnessinc@gmail.com";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, fibnessEmail);
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, "");

        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i, "Choose an email"));

    }

    /*NO FUNCIONA CAMBIAR IDIOMA*/
    private void showChangeLanguageDialog() {
        final String[] listLanguages = {"English", "Spanish"};
        AlertDialog.Builder message = new AlertDialog.Builder(this);
        message.setTitle("Choose Language");
        message.setSingleChoiceItems(listLanguages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    setLocale("en");
                    recreate();
                }
                else if(i == 1){
                    setLocale("es");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = message.create();
        alertDialog.show();
    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());;

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("myLang", lang);
        editor.apply();

    }

    private void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = preferences.getString("myLang", "");
        setLocale(lang);
    }




    private void saveSettingsData() {
        //falta por ver
    }

    /*code to test setting save
    method that store settings preferences
    private void saveData() {
        this class save the preferences
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("switchAge", switchAge.isChecked());
        editor.putBoolean("switchDistance", switchDistance.isChecked());
        editor.putBoolean("switchInvitation", switchInvitation.isChecked());
        editor.putBoolean("switchLike", switchLike.isChecked());
        editor.putBoolean("switchFollower", switchFollower.isChecked());
        editor.putBoolean("switchMessage", switchMessage.isChecked());

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    Load settings data that we have saved in preferences
    private void loadData(){
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        switchOn1 = preferences.getBoolean("switchAge", false);
        switchOn2 = preferences.getBoolean("switchDistance", false);
        switchOn3 = preferences.getBoolean("switchInvitation", false);
        switchOn4 = preferences.getBoolean("switchLike", false);
        switchOn5 = preferences.getBoolean("switchFollower", false);
        switchOn6 = preferences.getBoolean("switchMessage", false);
    }

    private void updateView(){
        switchAge.setChecked(switchOn1);
        switchDistance.setChecked(switchOn2);
        switchInvitation.setChecked(switchOn3);
        switchLike.setChecked(switchOn4);
        switchFollower.setChecked(switchOn5);
        switchMessage.setChecked(switchOn6);
    }
    */

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
