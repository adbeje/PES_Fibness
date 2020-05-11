package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class SelectedUserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ImageView backImgButton, blockImgButton;
    private TextView nFollowers, nFollowing, username,coma, age, country, description;
    private FloatingActionButton follow;
    private UserModel userModel;
    private UsersInfo ui = User.getInstance().getSelectedUser();
    private Boolean ImFolloing = false; /* ui.follow necesito por si el usuario en la misma pagina quiere seguir y dejar de seguir*/

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


        /*
        falta hacer set los valores de UI
        Boolean sigo = ui.follow;
        if(sigue){
            follow.setBackgroundTintList(ColorStateList.valueOf(-2818048)); //-2818048 = red color
        }
        else{
            System.out.println("NADA");;
            follow.setBackgroundTintList(ColorStateList.valueOf(-16021062)); //-16021062 = @color/c_icon_bkg_unsel
        }
        */


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
                else {
                    System.out.println("onBackPressed----2");
                    intent = new Intent(SelectedUserActivity.this, FollowersActivity.class);
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


    /*blockImgButton onClick*/
    public  void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_block_menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.item){
            Toast.makeText(this, "Inform a block message", Toast.LENGTH_SHORT).show();
            /*HACER UNA ADVERTENCIA, SI ACEPTA, BLOQUEA AL USUARIO*/
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if(getIntent().getStringExtra("name").equals("SearchUserActivity")){
            System.out.println("onBackPressed----1");
            intent = new Intent(SelectedUserActivity.this, SearchUsersActivity.class);
        }
        else {
            System.out.println("onBackPressed----2");
            intent = new Intent(SelectedUserActivity.this, FollowersActivity.class);
        }
        startActivity(intent);

    }


}
