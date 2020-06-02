package com.pes.fibness;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {

    private TextView achievements;
    private Dialog dialog;
    private Button btnLetGo, btShare;
    private ImageView btnBack, closePopup;

    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;
    private List<MyModel> models;
    private Integer [] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private TextView textNum, meterTraveled, message;
    private ProgressBar progressBar;

    private ArrayList<Achievement> achieves = User.getInstance().getAchievements();
    private int userDistance = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        achievements = findViewById(R.id.achievements);
        dialog = new Dialog(this);
        textNum = findViewById(R.id.textNum);
        btnBack = findViewById(R.id.backImgButton);
        btShare = findViewById(R.id.btn_share);
        progressBar = findViewById(R.id.progressBar);
        meterTraveled = findViewById(R.id.meterTraveled);
        message = findViewById(R.id.message);

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMessage();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();

            }
        });


        /*TEST*/
        ArrayList<Achievement> a = new ArrayList<>();
        Achievement a1;
        for(int i=0; i<4; ++i){
            a1 = new Achievement();
            a1.id = i;
            a1.active = false;
            a1.distance = i * 20;
            a.add(a1);
        }
        User.getInstance().setAchievements(a);


        models = new ArrayList<>();
        models.add(new MyModel(R.drawable.basecamp, "BASE CAMP", "START", "You started the adventure on " + User.getInstance().getRegisterDate().substring(0,10)));
        models.add(new MyModel(R.drawable.kirkjufell, "KIRKJUFELL", "463 M", ""));
        models.add(new MyModel(R.drawable.elcapitan, "EL CAPITAN", "2 307 M", ""));
        models.add(new MyModel(R.drawable.mountolympus, "MOUNT OLYMPUS", "2 918 M", ""));
        models.add(new MyModel(R.drawable.fitzroy, "FITZ ROY", "3 359 M", ""));

        adapterViewPager = new AdapterViewPager(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPadding(130,0,130,0);

        Integer [] colors_temp = {getResources().getColor(R.color.color1), getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color3),getResources().getColor(R.color.color1), getResources().getColor(R.color.color4)};
        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("slide number: " + position);
                if(position < (adapterViewPager.getCount()-1) && position < (colors.length -1)){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position+1]));

                    if(position == 0) {
                        textNum.setText("01");
                        btShare.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        meterTraveled.setVisibility(View.INVISIBLE);
                        message.setVisibility(View.INVISIBLE);
                    }
                    else if(position == 1) {
                        textNum.setText("02");
                        btShare.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        meterTraveled.setVisibility(View.VISIBLE);
                        achieves = User.getInstance().getAchievements();
                        message.setVisibility(View.INVISIBLE);
                        /*
                        System.out.println("userDistance: " + userDistance);
                        System.out.println("size: " + achieves.size() );
                        System.out.println("acheive distances: " + achieves.get(0).distance);
                        */
                        progressBar.setProgress((userDistance*100)/433);
                        meterTraveled.setText(""+ achieves.get(0).distance + "M");

                    }
                    else if(position == 2){
                        textNum.setText("03");
                        if (achieves.get(1).active) {
                            btShare.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            meterTraveled.setVisibility(View.VISIBLE);
                            progressBar.setProgress((userDistance*100)/2307);
                            meterTraveled.setText(""+ achieves.get(1).distance + "M");
                        }
                        else{
                            btShare.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            meterTraveled.setVisibility(View.INVISIBLE);
                            message.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(position == 3) {
                        textNum.setText("04");
                        if (achieves.get(2).active) {
                            btShare.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            meterTraveled.setVisibility(View.VISIBLE);
                            progressBar.setProgress((userDistance*100)/2918);
                            meterTraveled.setText(""+ achieves.get(2).distance + "M");
                        }
                        else{
                            btShare.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            meterTraveled.setVisibility(View.INVISIBLE);
                            message.setVisibility(View.VISIBLE);
                        }
                    }

                }
                else {
                    viewPager.setBackgroundColor(colors[colors.length-1]);
                    textNum.setText("05"); //the last one
                    if (achieves.get(3).active) {
                        btShare.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        meterTraveled.setVisibility(View.VISIBLE);
                        progressBar.setProgress((userDistance*100)/3359);
                        meterTraveled.setText(""+ achieves.get(3).distance + "M");
                    }
                    else{
                        btShare.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        meterTraveled.setVisibility(View.INVISIBLE);
                        message.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });









    }


    private void showPopupMessage() {
        dialog.setContentView(R.layout.popup);
        closePopup =  (ImageView) dialog.findViewById(R.id.closePopup);
        btnLetGo = (Button) dialog.findViewById(R.id.btn_letsGo);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnLetGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }


}
