package com.pes.fibness.ui.dietas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pes.fibness.R;

public class DietasFragment extends Fragment {

    private DietasViewModel dietasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dietasViewModel =
                ViewModelProviders.of(this).get(DietasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dietas, container, false);
        return root;
    }
}
