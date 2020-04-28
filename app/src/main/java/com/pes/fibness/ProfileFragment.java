package com.pes.fibness;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import static com.pes.fibness.R.id.iv_user;

public class ProfileFragment extends Fragment {

    private TextView username, nFollowers, nFollowing, nPost;
    private User u = User.getInstance();
    ImageView ivUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        Context thiscontext = container.getContext();

        username = root.findViewById(R.id.username);
        nFollowers = root.findViewById(R.id.nFollowers);
        nFollowing = root.findViewById(R.id.nFollowing);
        nPost = root.findViewById(R.id.nPost);
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
        nFollowers.setText(String.valueOf(User.getInstance().getnFollower()));
        nFollowing.setText(String.valueOf(User.getInstance().getnFollowing()));
        nPost.setText(String.valueOf(User.getInstance().getnPost()));
        if (validImage) {
            Glide.with(ProfileFragment.this)
                    .load(userImage)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUser);
        }
    }

}
