package com.pes.fibness;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class CreateDietActivity extends AppCompatActivity {

    private Boolean isNew;
    private String titleDiet = "";
    private ListView foodList;
    private ArrayList<Meal> food = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_diet);
        getExtras();

        food = User.getInstance().getMealList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCD);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titleDiet);
        getSupportActionBar().setSubtitle(User.getInstance().getDietDesc(titleDiet));

        foodList = (ListView) findViewById(R.id.FoodList);

        refreshList();

        Button add_ex = (Button) findViewById(R.id.AddFood);
        add_ex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNewFood();
                isNew = false;
                refreshList();
            }
        });
        
        foodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditExBox(position);
                return true;
            }
        });

    }

    private void showEditExBox(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateDietActivity.this);
        builder.setView(R.layout.input_edit_food);
        builder.setTitle("Food");
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText txtName = (EditText) dialog.findViewById(R.id.FoodTitle_edit);
        txtName.setText(food.get(position).name);
        final EditText numCalories = (EditText) dialog.findViewById(R.id.num_Calories_edit);
        numCalories.setText(food.get(position).id);
        final EditText numHour = (EditText) dialog.findViewById(R.id.num_Hour_edit);
        numHour.setText(food.get(position).time);
        Button btndone = (Button) dialog.findViewById(R.id.btn_doneFood_edit);
        Button btndelete = (Button) dialog.findViewById(R.id.btn_deleteFood_edit);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = true;
                if (txtName.getText().toString().trim().length() == 0) {
                    txtName.setError("Please, add a name");
                    correct = false;
                }
                if (numCalories.getText().toString().trim().length() == 0) {
                    numCalories.setError("Please, add a number");
                    correct = false;
                }
                if (numHour.getText().toString().trim().length() == 0) {
                    numHour.setError("Please, add a time");
                    correct = false;
                }
                if (correct) {
                    Meal d2 = new Meal();
                    d2.name = txtName.getText().toString();
                    d2.id = Integer.getInteger(numCalories.getText().toString());
                    d2.time = numHour.getText().toString();
                    food.set(position, d2);
                    User.getInstance().setMealList(food);
                    refreshList();
                    dialog.dismiss();
                }
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.remove(position);
                User.getInstance().setMealList(food);
                refreshList();
                dialog.dismiss();
            }
        });

    }

    private void refreshList() {
        foodList.setAdapter(new Food_Adap(this, food, isNew));
    }

    private void showNewFood() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateDietActivity.this);
        builder.setView(R.layout.input_new_food);
        builder.setTitle("Food");
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText txtName = (EditText) dialog.findViewById(R.id.FoodTitle);
        final EditText numCalories = (EditText) dialog.findViewById(R.id.num_Calories);
        final EditText numHour = (EditText) dialog.findViewById(R.id.num_Hour);
        Button bt = (Button) dialog.findViewById(R.id.btn_doneFood);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = true;
                if (txtName.getText().toString().trim().length() == 0) {
                    txtName.setError("Please, add a name");
                    correct = false;
                }
                if (numCalories.getText().toString().trim().length() == 0) {
                    numCalories.setError("Please, add a number");
                    correct = false;
                }
                if (numHour.getText().toString().trim().length() == 0) {
                    numHour.setError("Please, add a time");
                    correct = false;
                }
                if (correct) {
                    Meal d2 = new Meal();
                    d2.name = txtName.getText().toString();
                    d2.id = Integer.valueOf(numCalories.getText().toString());
                    d2.time = numHour.getText().toString();
                    food.add(d2);
                    User.getInstance().setMealList(food);
                    refreshList();
                    dialog.dismiss();
                }
            }
        });
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        isNew = extras.getBoolean("new");
        titleDiet = extras.getString("title");
    }

}
