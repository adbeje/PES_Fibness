package com.pes.fibness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        backButton = (ImageView) findViewById(R.id.backImgButton);

        BarChart barChart = (BarChart) findViewById(R.id.chart);
        /*
        Description d = new Description();
        d.setEnabled(true);
        barChart.setDescription(d);
        */

        final String[] week = new String[] {"Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday", "Sunday"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyValueFormatter(week));
        barChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        List<BarEntry> distances = new ArrayList<>();
        List<BarEntry> calories = new ArrayList<>();

        distances.add(new BarEntry(0f, 80f));
        distances.add(new BarEntry(2f, 80f));
        distances.add(new BarEntry(4f, 80f));
        distances.add(new BarEntry(6f, 80f));
        distances.add(new BarEntry(8f, 80f));
        distances.add(new BarEntry(10f, 80f));
        distances.add(new BarEntry(12f, 80f));

        calories.add(new BarEntry(1f, 20f));
        calories.add(new BarEntry(3f, 20f));
        calories.add(new BarEntry(5f, 20f));
        calories.add(new BarEntry(7f, 20f));
        calories.add(new BarEntry(9f, 20f));
        calories.add(new BarEntry(11f, 20f));
        calories.add(new BarEntry(13f, 20f));


        /*Create 2 datasets*/
        BarDataSet setDistance = new BarDataSet(distances, "Distance");
        setDistance.setColor(Color.rgb(78,186,221));
        BarDataSet setCalorie = new BarDataSet(calories, "Calorie");
        setCalorie.setColor(Color.rgb(140, 221, 159));
        //setCalorie.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(setDistance, setCalorie);
        //data.setBarWidth(0.75f); set custom bar width
        barChart.setData(data);
        barChart.animateXY(2000, 2000);
        barChart.invalidate(); //refresh





        /*to go back*/
        backButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });










    }



}
