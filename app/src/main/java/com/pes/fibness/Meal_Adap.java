package com.pes.fibness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Meal_Adap extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Context context;
    private ArrayList<Meal> dades;
    private boolean New;

    public Meal_Adap(Context c, ArrayList<Meal> m, boolean IsNew){
        context = c;
        dades = m;
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
        final View vista = inflater.inflate(R.layout.meal_element, null);
        if(!New) {
            TextView titulo = (TextView) vista.findViewById(R.id.txtNameMeal);
            TextView hora = (TextView) vista.findViewById(R.id.txtSetHour);
            titulo.setText(dades.get(position).name);
            String time = dades.get(position).time;
            String[] time2 = time.split(":");
            hora.setText(time2[0] + ":" + time2[1]);
        }

        return vista;
    }
}
