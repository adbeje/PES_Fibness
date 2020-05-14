package com.pes.fibness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Exercise_Adap extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Context context;
    private ArrayList<Exercise> dades;
    private boolean New;

    public Exercise_Adap(Context c, ArrayList<Exercise> d, boolean IsNew){
        context = c;
        dades = d;
        New = IsNew;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dades.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.exerciselist_element, null);
        if(!New) {
            TextView titulo = (TextView) vista.findViewById(R.id.ExerciseTitle);
            TextView repet = (TextView) vista.findViewById(R.id.txtRepet);
            TextView rest = (TextView) vista.findViewById(R.id.txtRest);
            TextView series = (TextView) vista.findViewById(R.id.txtSeries);
            ImageView image = (ImageView) vista.findViewById(R.id.photo_ex);
            TextView Desc = (TextView) vista.findViewById(R.id.txtDesc);
            titulo.setText(dades.get(position).TitleEx);
            int id = getImageID (dades.get(position).TitleEx);
            if(id != -1) image.setImageResource(id);
            else{
                image.setImageResource(R.drawable.logoazulflojo);
                Desc.setText(dades.get(position).Desc);
            }
            series.setText("SERIES: " + dades.get(position).NumSerie);
            rest.setText("REST: " + dades.get(position).NumRest + "s");
            repet.setText("REPETITIONS: " + dades.get(position).NumRepet);
        }

        return vista;
    }

    private int getImageID(String name){
        int id = -1;
        switch (name){
            case "Squat":
                id = R.drawable.squat;
                break;
            case "Hollow Hold":
                id = R.drawable.hollow_hold;
                break;
            case "Lunges":
                id = R.drawable.lunges;
                break;
            case "Back Extensión Hold":
                id = R.drawable.back_extension_hold;
                break;
            case "Plank":
                id = R.drawable.plank;
                break;
            case "Sit ups":
                id = R.drawable.sit_ups;
                break;
            case "Jumping Jacks":
                id = R.drawable.jumping_jacks;
                break;
            case "Push Up":
                id = R.drawable.push_up;
                break;
            case "Dips":
                id = R.drawable.dips;
                break;
            case "Burpees":
                id = R.drawable.burpees;
                break;
            case "Leg Raises":
                id = R.drawable.ab_infer;
                break;
            case "Bicycle Crunches":
                id = R.drawable.bicycle_crunches;
                break;
        }
        return id;
    }

}
