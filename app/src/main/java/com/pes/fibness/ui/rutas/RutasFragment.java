package com.pes.fibness.ui.rutas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pes.fibness.R;

public class RutasFragment extends Fragment {

    private RutasViewModel rutasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rutasViewModel =
                ViewModelProviders.of(this).get(RutasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rutas, container, false);
        return root;
    }
}
