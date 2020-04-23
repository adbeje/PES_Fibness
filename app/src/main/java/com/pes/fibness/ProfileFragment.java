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

public class ProfileFragment extends Fragment {

    private ImageView imgSettings, editButton, imgViewProfile, imgAchievements;
    private TextView username, nFollowers, nFollowing, nPost;

    private Dialog dialog;
    Context thiscontext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        thiscontext = container.getContext();

        username = root.findViewById(R.id.username);
        nFollowers = root.findViewById(R.id.nFollowers);
        nFollowing = root.findViewById(R.id.nFollowing);
        nPost = root.findViewById(R.id.nPost);


        showUserInfo();


        imgSettings = (ImageView) root.findViewById(R.id.setting);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);

            }
        });

        editButton = (ImageView) root.findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        imgViewProfile = (ImageView) root.findViewById(R.id.viewProfile);
        imgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewProfileActivity.class);
                startActivity(i);

            }
        });


        dialog = new Dialog(thiscontext);
        imgAchievements = (ImageView) root.findViewById(R.id.achievements);
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
        username.setText(User.getInstance().getName());
        nFollowers.setText(String.valueOf(User.getInstance().getnFollower()));
        nFollowing.setText(String.valueOf(User.getInstance().getnFollowing()));
        nPost.setText(String.valueOf(User.getInstance().getnPost()));
    }

}
