package com.pes.fibness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.pes.fibness.R.id.backImgButton;
import static com.pes.fibness.R.id.confirm;
import static com.pes.fibness.R.id.et_Description;
import static com.pes.fibness.R.id.et_Name;
import static com.pes.fibness.R.id.et_mostrar_fecha_picker;
import static com.pes.fibness.R.id.ib_obtener_fecha;
import static com.pes.fibness.R.id.iv_user;
import static com.pes.fibness.R.id.s_Country;
import static com.pes.fibness.R.id.tB_fem;
import static com.pes.fibness.R.id.tb_male;

public class EditProfileActivity extends AppCompatActivity {
    User u = User.getInstance();

    /* Calendar */
    private static final String zero = "0";
    private static final String slash = "/";

    public final Calendar c = Calendar.getInstance();

    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    final int year = c.get(Calendar.YEAR);

    /* Profile picture */
    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;
    ImageView ivUser;
    private String [] items = {"Camera","Gallery"};


    /* Widgets */
    Spinner sCountry;
    EditText etDate, etName, etDescription;
    ImageButton ibDateGetter;
    ToggleButton tbFem;
    ToggleButton tbMale;
    private EasyImage easyImage;

    //Object to take pictures

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(et_Name);
        etDescription = findViewById(et_Description);
        sCountry = findViewById(s_Country);
        etDate = findViewById(et_mostrar_fecha_picker);
        ibDateGetter = findViewById(ib_obtener_fecha);
        tbFem = findViewById(tB_fem);
        tbMale = findViewById(tb_male);
        ivUser = findViewById(iv_user);


        showUserInfo();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[] {Manifest.permission.CAMERA}, 0);


        ImageView backButton = findViewById(backImgButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        EasyImage.configuration(this)
                .setImagesFolderName("EasyImage sample")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);

        ivUser.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(EditProfileActivity.this, "Select", 0);
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

        tbFem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbMale.setChecked(false);
                } else {
                    tbMale.setChecked(true);
                }
            }
        });

        tbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbFem.setChecked(false);
                } else {
                    tbFem.setChecked(true);
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                //u.setImage(imageFiles.get(0));
                Glide.with(EditProfileActivity.this)
                        .load(imageFiles.get(0))
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUser);
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
        etName.setText(u.getName());
        boolean validImage = false;
        File userImage = null;
        if (u.getImage() != null) {
            validImage = true;
            //userImage = u.getImage();
        }
        if(!u.getDescription().equals("null")) etDescription.setText(u.getDescription());
        if(!u.getBirthDate().equals("null")) etDate.setText(u.getBirthDate());
        if (!u.getGender().equals("null")) {
            if (u.getGender().equals("1")) tbFem.setChecked(true);
            else tbMale.setChecked(true);
        }
        if(!u.getCountry().equals("null")) sCountry.setSelection(Integer.parseInt(u.getCountry()));
        if (validImage) {
            Glide.with(EditProfileActivity.this)
                    .load(userImage)
                    .centerCrop()
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUser);
        }

    }

    private void saveEditData() {

        u.setName(etName.getText().toString());
        if(tbFem.isChecked())
            u.setGender("1");
        else u.setGender("0");
        u.setDescription(etDescription.getText().toString());
        u.setBirthDate(etDate.getText().toString());
        u.setCountry(String.valueOf(sCountry.getSelectedItemPosition()));



        String route = "http://10.4.41.146:3001/user/"+u.getId()+"/info";
        ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), route);
        connection.postUserInfo();
    }




}
