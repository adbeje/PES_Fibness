package com.pes.fibness;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SelectedUserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ImageView backImgButton, blockImgButton;
    private TextView nFollowers, nFollowing, username,coma, age, country, description;
    private FloatingActionButton follow;
    private UserModel userModel;
    private UsersInfo ui = User.getInstance().getSelectedUser();
    private Boolean ImFolloing = ui.follow; /* ui.follow necesito por si el usuario en la misma pagina quiere seguir y dejar de seguir*/



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);

        backImgButton = findViewById(R.id.backImgButton);
        blockImgButton = findViewById(R.id.blockImgButton);
        nFollowers = findViewById(R.id.nFollowers);
        nFollowing = findViewById(R.id.nFollowing);
        username = findViewById(R.id.username);
        coma = findViewById(R.id.coma);
        age = findViewById(R.id.age);
        country = findViewById(R.id.country);
        description = findViewById(R.id.description);
        follow = findViewById(R.id.follow);

        if(ImFolloing){
            follow.setBackgroundTintList(ColorStateList.valueOf(-2818048)); //-2818048 = red color
        }
        else{
            System.out.println("NADA");;
            follow.setBackgroundTintList(ColorStateList.valueOf(-16021062)); //-16021062 = @color/c_icon_bkg_unsel
        }

        /*falta cargar imagen*/
        nFollowers.setText(""+ ui.nFollower);
        nFollowing.setText(""+ui.nFollowing);
        username.setText(ui.username);
        if(ui.birthDate == null)
            coma.setText("");
        else {
            if(ui.sAge)
                age.setText(howManyYears(ui.birthDate));
            else coma.setText("");
        }
        String[] planets = getResources().getStringArray(R.array.countries);
        if(ui.country.equals("null"))
            country.setText("");
        else country.setText(planets[Integer.parseInt(ui.country)]);
        if(ui.description.equals("null"))
            description.setText("");
        else description.setText(ui.description);



        Intent intent = getIntent();
        if(intent.getExtras() != null){
            userModel = (UserModel) intent.getSerializableExtra("data");
        }



        backImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(getIntent().getStringExtra("name").equals("SearchUserActivity")){
                    System.out.println("onBackPressed----1");
                    intent = new Intent(SelectedUserActivity.this, SearchUsersActivity.class);
                }
                else if(getIntent().getStringExtra("name").equals("FollowersActivity")) {
                    System.out.println("onBackPressed----2");
                    intent = new Intent(SelectedUserActivity.this, FollowersActivity.class);
                }
                else{
                    /*hay que cargar otravez*/
                    intent = new Intent(SelectedUserActivity.this, FollowingActivity.class);
                }
                startActivity(intent);

            }
        });





        /*follow button*/
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("My backgroud: " + view.getBackgroundTintList());
                if(!ImFolloing){
                    //follow user
                    ConnetionAPI connetionAPI = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/follow");
                    connetionAPI.followUser(User.getInstance().getId(), userModel.getId());

                    follow.setBackgroundTintList(ColorStateList.valueOf(-2818048)); //-2818048 = red color
                    ImFolloing = true;
                }
                else{
                    //delete follow
                    ConnetionAPI connetionAPI = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/follow/" + User.getInstance().getId() + "/" + userModel.getId());
                    connetionAPI.deleteFollowing();

                    view.setBackgroundTintList(ColorStateList.valueOf(-16021062)); //-16021062 = @color/c_icon_bkg_unsel
                    ImFolloing = false;
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String howManyYears(String birthDate){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNac = LocalDate.parse(birthDate, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        System.out.printf("Tu edad es: %s años, %s meses y %s días",
                periodo.getYears(), periodo.getMonths(), periodo.getDays());
        return String.valueOf(periodo.getYears());
    }




    /*blockImgButton onClick*/
    public  void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_block_menu);
        popupMenu.show();
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.bk_item){
            Toast.makeText(this, "Inform a block message", Toast.LENGTH_SHORT).show();
            showMessage();
            return true;
        }
        return false;
    }

    private void showMessage() {
        AlertDialog.Builder message = new AlertDialog.Builder(this);
        message.setTitle(getResources().getString(R.string.bkUser));
        message.setMessage(getResources().getString(R.string.bkMsg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConnetionAPI connetionAPI = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/block");
                        connetionAPI.blockUser(User.getInstance().getId(), userModel.getId());
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = message.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if(getIntent().getStringExtra("name").equals("SearchUserActivity")){
            System.out.println("onBackPressed----1");
            intent = new Intent(SelectedUserActivity.this, SearchUsersActivity.class);
        }
        else if(getIntent().getStringExtra("name").equals("FollowersActivity")) {
            System.out.println("onBackPressed----2");
            intent = new Intent(SelectedUserActivity.this, FollowersActivity.class);
        }
        else{
            /*hay que cargar otravez*/
            intent = new Intent(SelectedUserActivity.this, FollowingActivity.class);
        }
        startActivity(intent);

    }








}
