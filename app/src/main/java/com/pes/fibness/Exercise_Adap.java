package com.pes.fibness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Exercise_Adap extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Context context;
    private String[][] dades;
    private boolean New;

    public Exercise_Adap(Context c, String[][] d, boolean IsNew){
        context = c;
        dades = d;
        New = IsNew;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dades.length;
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
            titulo.setText(dades[position][0]);
            repet.setText("SERIES: " + dades[position][1]);
            rest.setText("REST: " + dades[position][2] + "s");
        }

        return vista;
    }
}
