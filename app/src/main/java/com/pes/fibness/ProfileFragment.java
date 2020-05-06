package com.pes.fibness;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import static com.pes.fibness.R.id.iv_user;

public class ProfileFragment extends Fragment {

    private TextView username, chat, users;
    private User u = User.getInstance();
    ImageView ivUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        Context thiscontext = container.getContext();

        username = root.findViewById(R.id.username);
        chat = root.findViewById(R.id.chat);
        users = root.findViewById(R.id.userModels);
        ivUser = root.findViewById(iv_user);


        showUserInfo();


        ImageView imgSettings = (ImageView) root.findViewById(R.id.setting);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);

            }
        });

        ImageView editButton = (ImageView) root.findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        ImageView imgViewProfile = (ImageView) root.findViewById(R.id.viewProfile);
        imgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewProfileActivity.class);
                startActivity(i);

            }
        });


        Dialog dialog = new Dialog(thiscontext);
        ImageView imgAchievements = (ImageView) root.findViewById(R.id.achievements);
        imgAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), AchievementsActivity.class);
                startActivity(i);

            }
        });


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*hay que cargar (id, nombre) de los usuario, excepto el que solicita */
                Intent i = new Intent(getActivity(), SearchUsersActivity.class);
                startActivity(i);
            }
        });








        return root;
    }

    private void showUserInfo() {
        boolean validImage = false;
        File userImage = null;
        if (u.getImage() != null) {
            validImage = true;
            userImage = u.getImage();
        }
        username.setText(u.getName());
        if (validImage) {
            Glide.with(ProfileFragment.this)
                    .load(userImage)
                    .centerCrop()
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUser);
        }
    }

}