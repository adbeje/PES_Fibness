package com.pes.fibness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Quota;

public class TrainingCommentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button importTraining;
    private ImageView like, comment ;
    private TextView nlike, ncomment;
    private ConstraintLayout CL;

    private List<ExerciseModel> exerciseModelList = new ArrayList<>();
    private ArrayList<ExerciseExtra> names = new ArrayList<>();
    private ShowExerciseAdapter showExerciseAdapter;

    private TrainingModel trainingModel;
    private int userId;
    private Boolean liked = false; //User.get...getLikeElement()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_comment);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            trainingModel = (TrainingModel) intent.getSerializableExtra("data");
            userId = (int) intent.getSerializableExtra("userId");
            System.out.println("Training id: " + trainingModel.getId());
            System.out.println("User selected id: " + userId);
        }

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        importTraining = findViewById(R.id.importTraining);
        like = findViewById(R.id.like);
        nlike = findViewById(R.id.nlike);
        comment = findViewById(R.id.comment);
        ncomment = findViewById(R.id.ncomment);
        CL = findViewById(R.id.CL);
        nlike.setText("" + trainingModel.getnLikes());
        ncomment.setText("" + trainingModel.getnComment());


        ArrayList<ExerciseExtra> tn = User.getInstance().getExerciseExtras();
        names = tn;

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        for(int i=0; i < names.size(); ++i){
            ExerciseModel exerciseModel = new ExerciseModel(names.get(i).id, names.get(i).title, names.get(i).desc, names.get(i).numRep, names.get(i).numSerie, names.get(i).numRest);
            exerciseModelList.add(exerciseModel);
        }


        showExerciseAdapter = new ShowExerciseAdapter(exerciseModelList);
        recyclerView.setAdapter(showExerciseAdapter);



        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(liked){ //delete like
                    ConnetionAPI connetionAPI = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/like/" + userId + "/" + trainingModel.getId() + "/element" );
                    connetionAPI.deleteElementLike();
                    int n = trainingModel.getnLikes();
                    trainingModel.setnLikes(n-1);
                    nlike.setText("" + trainingModel.getnLikes());
                    like.setColorFilter(getApplicationContext().getResources().getColor(R.color.c_icon_like));
                    liked = false;
                }
                else { //post like
                    ConnetionAPI connetionAPI = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/user/like");
                    connetionAPI.likeElement(userId, trainingModel.getId(), "element");
                    int n = trainingModel.getnLikes();
                    trainingModel.setnLikes(n+1);
                    nlike.setText("" + trainingModel.getnLikes());
                    like.setColorFilter(getApplicationContext().getResources().getColor(R.color.red));
                    liked = true;
                }

            }
        });

    }

}


class ShowExerciseAdapter extends RecyclerView.Adapter<ShowExerciseAdapter.ShowExerciseAdapterVh> {

    private List<ExerciseModel> exerciseModelList;
    private Context context;
    private Boolean first = false;

    public ShowExerciseAdapter(List<ExerciseModel> exerciseModelList) {
        this.exerciseModelList = exerciseModelList;
    }


    @NonNull
    @Override
    public ShowExerciseAdapter.ShowExerciseAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ShowExerciseAdapter.ShowExerciseAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_exercise, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowExerciseAdapter.ShowExerciseAdapterVh holder, int position) {
        ExerciseModel exerciseModel = exerciseModelList.get(position);
        String title = exerciseModel.getTitle();
        String description = exerciseModel.getDesc();
        int numRep = exerciseModel.getNumRep();
        int numSerie = exerciseModel.getNumSerie();
        int numRest = exerciseModel.getNumRest();

        switch (title){
            case "Squat":
                holder.imageView.setImageResource(R.drawable.squat);
                break;
            case "Hollow Hold":
                holder.imageView.setImageResource( R.drawable.hollow_hold);
                break;
            case "Lunges":
                holder.imageView.setImageResource(R.drawable.lunges);
                break;
            case "Back Extensión Hold":
                holder.imageView.setImageResource(R.drawable.back_extension_hold);
                break;
            case "Plank":
                holder.imageView.setImageResource(R.drawable.plank);
                break;
            case "Sit ups":
                holder.imageView.setImageResource(R.drawable.sit_ups);
                break;
            case "Jumping Jacks":
                holder.imageView.setImageResource(R.drawable.jumping_jacks);
                break;
            case "Push Up":
                holder.imageView.setImageResource(R.drawable.push_up);
                break;
            case "Dips":
                holder.imageView.setImageResource(R.drawable.dips);
                break;
            case "Burpees":
                holder.imageView.setImageResource(R.drawable.burpees);
                break;
            case "Leg Raises":
                holder.imageView.setImageResource(R.drawable.ab_infer);
                break;
            case "Bicycle Crunches":
                holder.imageView.setImageResource(R.drawable.bicycle_crunches);
                break;
        }

        holder.tvTitle.setText(title);
        String s1 = (String) holder.tvRep.getText();
        holder.tvRep.setText(s1 + " " + numRep);
        String s2 = (String) holder.tvSerie.getText();
        holder.tvSerie.setText(s2 + " " + numSerie);
        String s3 = (String) holder.tvRest.getText();
        holder.tvRest.setText(s3 + " " + numRest + "s");
        holder.tvDesc.setText(description);
    }

    @Override
    public int getItemCount() {
        return exerciseModelList.size();
    }



    public class ShowExerciseAdapterVh extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRep, tvSerie, tvRest, tvDesc;
        ImageView imageView;

        public ShowExerciseAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvDesc = itemView.findViewById(R.id.desc);
            tvRep = itemView.findViewById(R.id.reps);
            tvSerie = itemView.findViewById(R.id.serie);
            tvRest = itemView.findViewById(R.id.rest);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }




}