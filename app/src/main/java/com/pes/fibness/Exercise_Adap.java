package com.pes.fibness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            titulo.setText(dades.get(position).TitleEx);
            series.setText("SERIES: " + dades.get(position).NumSerie);
            rest.setText("REST: " + dades.get(position).NumRest + "s");
            repet.setText("REPETITIONS: " + dades.get(position).NumRepet);
        }

        return vista;
    }
}
