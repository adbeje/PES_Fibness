package com.pes.fibness;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pes.fibness.ui.concreteTraining.ConcreteTrainingFragment;


public class CreateTrainingActivity extends AppCompatActivity {

    Fragment ctf = new ConcreteTrainingFragment();
    private Boolean IsNew;
    private String TilteTraining = "";
    TextView title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_training);

        getExtras();

        title = (TextView) findViewById(R.id.title);

        if(IsNew){
            showInputBox();
        }
        else{
            title.setText(TilteTraining);
        }
        Toolbar toolbar = (Toolbar)findViewById (R.id.toolbarCT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        System.out.println("HOLAA " + TilteTraining);
    }

    private void getExtras(){
        Bundle extras = getIntent().getExtras();
        IsNew = extras.getBoolean("new");
        TilteTraining = extras.getString("title");
    }

    public void showInputBox(){
        final Dialog dialog = new Dialog(CreateTrainingActivity.this);
        dialog.setTitle("Training");
        dialog.setContentView(R.layout.input_title_box);
        TextView txt = (TextView) dialog.findViewById(R.id.inputboxTitleTraining);
        txt.setText("Add a name");
        final EditText editText = (EditText)dialog.findViewById(R.id.TitleTraininginput);
        editText.setText("Name");
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TilteTraining = editText.getText().toString();
                title.setText(TilteTraining);
                dialog.dismiss();
            }
        });
        dialog.show();
        System.out.println("Aqui llega");
    }
}
