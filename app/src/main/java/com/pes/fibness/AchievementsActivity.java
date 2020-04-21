package com.pes.fibness;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView textNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        achievements = findViewById(R.id.achievements);
        dialog = new Dialog(this);
        textNum = findViewById(R.id.textNum);
        btnBack = findViewById(R.id.backImgButton);
        btShare = findViewById(R.id.btn_share);

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMessage();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementsActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });



        models = new ArrayList<>();
        models.add(new MyModel(R.drawable.basecamp, "BASE CAMP", "START", "You started the adventure on" + "-User register date"));
        models.add(new MyModel(R.drawable.kirkjufell, "KIRKJUFELL", "463 M", ""));
        models.add(new MyModel(R.drawable.elcapitan, "EL CAPITAN", "2 307 M", "The best views are achieved after a hard climb."));
        models.add(new MyModel(R.drawable.mountolympus, "MOUNT OLYMPUS", "2 918 M", "The best views are achieved after a hard climb."));
        models.add(new MyModel(R.drawable.fitzroy, "FITZ ROY", "3 359 M", "The best views are achieved after a hard climb."));

        adapterViewPager = new AdapterViewPager(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPadding(130,0,130,0);

        Integer [] colors_temp = {getResources().getColor(R.color.color1), getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color3),getResources().getColor(R.color.color1), getResources().getColor(R.color.color4)};
        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("slide number: " + position);
                if(position < (adapterViewPager.getCount()-1) && position < (colors.length -1)){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position+1]));

                    if(position == 0) textNum.setText("01");
                    else if(position == 1) textNum.setText("02");
                    else if(position == 2) textNum.setText("03");
                    else if(position == 3) textNum.setText("04");
                    System.out.println("text num: " + textNum.getText());

                }
                else {
                    viewPager.setBackgroundColor(colors[colors.length-1]);
                    textNum.setText("05"); //the last one
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
