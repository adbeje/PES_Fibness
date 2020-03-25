package com.pes.fibness.ui.entrenamientos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pes.fibness.R;
import com.pes.fibness.ui.concreteTraining.ConcreteTrainingFragment;
import com.pes.fibness.ui.dietas.DietasViewModel;

import java.util.ArrayList;
import java.util.List;

public class EntrenamientosFragment extends Fragment {

    Fragment entrenamentP = new ConcreteTrainingFragment();
    private EntrenamientosViewModel entrenamientosViewModel;
    private ListView listViewT;
    private ArrayList<String> TrainingList = new ArrayList<String>();
    private View view;
    private boolean firstTime = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        entrenamientosViewModel = ViewModelProviders.of(this).get(EntrenamientosViewModel.class);

        View view = inflater.inflate(R.layout.fragment_entrenamientos, container, false);


        if(firstTime){
            for(int i = 0; i < 24; i++){
                TrainingList.add("Training " + i);
            }
            firstTime = false;
        }

        listViewT = (ListView)view.findViewById(R.id.listViewTraining);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row, TrainingList);

        listViewT.setAdapter(listViewAdapter);
        /*FragmentTransaction ft2 = getActivity().getSupportFragmentManager().beginTransaction();
        ft2.replace(R.id.frame_training2, llistaEntrenament);
        ft2.commit();*/

        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.training_fragment, entrenamentP);
                ft.addToBackStack(null);
                ft.commit();
            }
        } );

        Button button = (Button) view.findViewById(R.id.FakeFloatingButton);

        button.setOnClickListener( new AdapterView.OnClickListener() {
            public void onClick(View v){
                System.out.println("Holii");
                TrainingList.add("New Training");
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row, TrainingList);

                listViewT.setAdapter(listViewAdapter);
            }
        });

        return view;
    }

}
