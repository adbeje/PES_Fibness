package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ChooseDayActivity extends AppCompatActivity {

    private ListView diasList;
    private ArrayList<String> listaDias;
    private String titleDiet = "";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_diet);
        getExtras();


        listaDias = User.getInstance().getDias();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titleDiet);
        getSupportActionBar().setSubtitle(User.getInstance().getDietDesc(titleDiet));

        diasList = (ListView) findViewById(R.id.DaysList);

        refreshList();

        diasList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dia = getDia(position);
                int idDiet = User.getInstance().getDietID(titleDiet);
                ConnetionAPI c = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/diet/" + idDiet + "/" +
                        dia);
                c.getDietMeal(titleDiet, dia);
            }
        } );

    }

    private void refreshList() {
        diasList.setAdapter(new ArrayAdapter<String>(this, R.layout.row, listaDias));
    }

    private String getDia(int position){
        String dia ="";
        switch (position) {
            case 0:
                dia = "lunes";
                break;
            case 1:
                dia = "martes";
                break;
            case 2:
                dia = "miercoles";
                break;
            case 3:
                dia = "jueves";
                break;
            case 4:
                dia = "viernes";
                break;
            case 5:
                dia = "sabado";
                break;
            case 6:
                dia = "domingo";
                break;
        }
        return dia;
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        titleDiet = extras.getString("title");
    }

}
