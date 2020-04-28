package com.pes.fibness;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static com.pes.fibness.R.id.*;
import static com.pes.fibness.R.id.et_Description;
import static com.pes.fibness.R.id.et_Name;
import static com.pes.fibness.R.id.s_Country;

public class EditProfileActivity extends AppCompatActivity {
    private static final String zero = "0";
    private static final String slash = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    final int year = c.get(Calendar.YEAR);

    //Widgets
    Spinner sCountry;
    EditText etDate, etName, etDescription;
    ImageButton ibDateGetter;
    ToggleButton tb_fem;
    ToggleButton tb_mal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(et_Name);
        etDescription = findViewById(et_Description);
        sCountry = findViewById(s_Country);
        etDate = findViewById(et_mostrar_fecha_picker);
        ibDateGetter = findViewById(ib_obtener_fecha);
        tb_fem = findViewById(tB_fem);
        tb_mal = findViewById(tb_male);


        showUserInfo();


        ImageView backButton = (ImageView) findViewById(backImgButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        TextView doneButton = findViewById(confirm);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                saveEditData();
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ImageView calendarButton = findViewById(ib_obtener_fecha);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                getDate();

            }
        });

        tb_fem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tb_mal.setChecked(false);
                } else {
                    tb_mal.setChecked(true);
                }
            }
        });

        tb_mal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tb_fem.setChecked(false);
                } else {
                    tb_fem.setChecked(true);
                }
            }

        });

    }

    private void getDate(){
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String formatDay = (dayOfMonth < 10)? zero + dayOfMonth :String.valueOf(dayOfMonth);
                String formatMonth = (mesActual < 10)? zero + mesActual :String.valueOf(mesActual);
                etDate.setText(formatDay + slash + formatMonth + slash + year);
            }
        }, year, month, day);
        datePicker.show();

    }

    private void showUserInfo() {

        /*etName.setText("Jose Luis");
        etDescription.setText("Me gusta cagarme encima");
        int i = 30;
        sCountry.setSelection(i);*/
        User u = User.getInstance();
        etName.setText(u.getName());
        if(!u.getDescription().equals("null")) etDescription.setText(u.getDescription());
        if(!u.getBirthDate().equals("null")) etDate.setText(u.getBirthDate());
        if (!u.getGender().equals("null")) {
            if (u.getGender().equals("1")) tb_fem.setChecked(true);
            else tb_mal.setChecked(true);
        }
        if(!u.getCountry().equals("null")) sCountry.setSelection(Integer.parseInt(u.getCountry()));

    }

    private void saveEditData() {
        User u = User.getInstance();
        u.setName(etName.getText().toString());
        if(tb_fem.isChecked())
            u.setGender("1");
        else u.setGender("0");
        u.setDescription(etDescription.getText().toString());
        u.setBirthDate(etDate.getText().toString());
        u.setCountry(String.valueOf(sCountry.getSelectedItemPosition()));



        String route = "http://10.4.41.146:3001/user/"+u.getId()+"/info";
        ConnetionAPI connetion = new ConnetionAPI(getApplicationContext(), route);
        connetion.postUserInfo();
    }




}
