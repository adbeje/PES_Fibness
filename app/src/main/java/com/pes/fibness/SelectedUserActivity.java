package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedUserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ImageView backImgButton, blockImgButton;
    private TextView nFollowers, nFollowing, username,coma, age, country, description;


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

        //tvUser = findViewById(R.id.selectedUser);

        backImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedUserActivity.this, SearchUsersActivity.class);
                startActivity(intent);
            }
        });




        Intent intent = getIntent();
        if(intent.getExtras() != null){
            UserModel userModel = (UserModel) intent.getSerializableExtra("data");
            //tvUser.setText(userModel.getUsername() + " " + userModel.getId());
        }


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
            return true;
        }
        return false;
    }
}
