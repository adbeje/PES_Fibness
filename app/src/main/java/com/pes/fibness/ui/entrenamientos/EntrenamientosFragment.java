package com.pes.fibness.ui.entrenamientos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pes.fibness.CreateTrainingActivity;
import com.pes.fibness.R;
import com.pes.fibness.User;
import com.pes.fibness.ui.concreteTraining.ConcreteTrainingFragment;

import java.util.ArrayList;

public class EntrenamientosFragment extends Fragment {

    Fragment entrenamentP = new ConcreteTrainingFragment();
    private EntrenamientosViewModel entrenamientosViewModel;
    private ListView listViewT;
    private ArrayList<String> TrainingList;
    private View view;
    private boolean firstTime = true;
    private String Trainin_Name = "";
    User u = new User();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        entrenamientosViewModel = ViewModelProviders.of(this).get(EntrenamientosViewModel.class);

        View view = inflater.inflate(R.layout.fragment_entrenamientos, container, false);
       // TrainingList = new ArrayList<String>();
        TrainingList = u.getTrainingList();
        listViewT = (ListView)view.findViewById(R.id.listViewTraining);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row, TrainingList);

        listViewT.setAdapter(listViewAdapter);

        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent TrainingPage = new Intent(getActivity(), CreateTrainingActivity.class);
                TrainingPage.putExtra("new", false);
                TrainingPage.putExtra("title", TrainingList.get(position));
                startActivity(TrainingPage);
            }
        } );

        Button button = (Button) view.findViewById(R.id.FakeFloatingButton);

        button.setOnClickListener( new AdapterView.OnClickListener() {
            public void onClick(View v){

                Intent TrainingPage = new Intent(getActivity(), CreateTrainingActivity.class);
                TrainingPage.putExtra("new", true);
                TrainingPage.putExtra("title", "");
                startActivity(TrainingPage);
            }
        });

        return view;
    }

}
