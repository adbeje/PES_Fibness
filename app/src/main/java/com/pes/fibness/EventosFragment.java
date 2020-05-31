package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pes.fibness.R;

public class EventosFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eventos, container, false);

        FloatingActionButton floatingActionButton = root.findViewById(R.id.fb_new_event);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_event = new Intent(getActivity(), CreateEventActivity.class);
                //create_event.putExtra("new", true);
                //create_event.putExtra("new", false);
                startActivity(create_event);
            }
        });
        return root;

    }
}
