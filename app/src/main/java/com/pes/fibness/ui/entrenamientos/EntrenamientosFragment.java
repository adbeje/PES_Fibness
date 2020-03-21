package com.pes.fibness.ui.entrenamientos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pes.fibness.R;

public class EntrenamientosFragment extends Fragment {

    private EntrenamientosViewModel entrenamientosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        entrenamientosViewModel =
                ViewModelProviders.of(this).get(EntrenamientosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_entrenamientos, container, false);
        return root;
    }
}
