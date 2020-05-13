package com.pes.fibness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Food_Adap extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Context context;
    private ArrayList<Aliment> dades;
    private boolean New;

    public Food_Adap(Context c, ArrayList<Aliment> d, boolean IsNew){
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
        final View vista = inflater.inflate(R.layout.aliment_element, null);
        if(New) {
            TextView txtCal = (TextView) vista.findViewById(R.id.txtCalories);
            txtCal.setText("");
        }
        else {
            TextView titulo = (TextView) vista.findViewById(R.id.txtNameAliment);
            TextView calorias = (TextView) vista.findViewById(R.id.txtSetCalories);
            titulo.setText(dades.get(position).name);
            calorias.setText(dades.get(position).calories);
        }

        return vista;
    }
}
