package com.pes.fibness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class BlockUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);
    }









    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BlockUserActivity.this, SettingsActivity.class);
        startActivity(intent);

    }

}