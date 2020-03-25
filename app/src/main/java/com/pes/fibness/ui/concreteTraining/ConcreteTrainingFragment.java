package com.pes.fibness.ui.concreteTraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pes.fibness.R;
import com.pes.fibness.ui.eventos.EventosViewModel;

public class ConcreteTrainingFragment extends Fragment {

    private  ConcreteTrainingViewModel concreteTrainingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        concreteTrainingViewModel = ViewModelProviders.of(this).get(ConcreteTrainingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_concrete_training, container, false);
        return root;
    }
}
