package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    int id;
    String title;
    String desc;
    String date;
    String hour;
    Point place;
    Boolean comunity;
    Boolean participa = false;
    int pos;

    Button delete;
    Button edit;
    Button join;
    FloatingActionButton participantes;

    MapView mapView;
    MapboxMap mapboxMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapBox_ACCESS_TOKEN));
        setContentView(R.layout.activity_view_event);
        getExtras();

        delete = findViewById(R.id.btn_delete_event);
        edit = findViewById(R.id.btn_edit_event);
        join = findViewById(R.id.btn_join_event);
        participantes = findViewById(R.id.fb_participantes);
        mapView = findViewById(R.id.mapEvent);

        ((TextView) findViewById(R.id.titleEvent)).setText(title);
        ((TextView) findViewById(R.id.descEvent)).setText(desc);
        ((TextView) findViewById(R.id.dateEvent)).setText(date);
        ((TextView) findViewById(R.id.hourEvent)).setText(hour);

        if(comunity){
            delete.setVisibility(View.INVISIBLE);
            delete.setClickable(false);
            edit.setVisibility(View.INVISIBLE);
            edit.setClickable(false);
            join.setVisibility(View.VISIBLE);
            join.setClickable(true);
            participa = User.getInstance().participa();
            if(participa){
                join.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                join.setText("Leave");
            }
            else{
                join.setBackground(getResources().getDrawable(R.drawable.btn_bg_sel));
                join.setText("Join");
            }
        }
        else{
            delete.setVisibility(View.VISIBLE);
            delete.setClickable(true);
            edit.setVisibility(View.VISIBLE);
            edit.setClickable(true);
            join.setVisibility(View.INVISIBLE);
            join.setClickable(false);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conectionAPI delete event
                User.getInstance().deleteEvent(pos);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modify_event = new Intent(EventActivity.this, CreateEventActivity.class);
                modify_event.putExtra("new", false);
                modify_event.putExtra("id", id);
                modify_event.putExtra("title", title);
                modify_event.putExtra("desc", desc);
                modify_event.putExtra("date", date);
                modify_event.putExtra("hour", hour);
                modify_event.putExtra("place", place);
                modify_event.putExtra("position", pos);
                startActivity(modify_event);
                finish();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(participa){
                    //conectionAPI delete participa
                    User.getInstance().deleteParticipa();
                    join.setBackground(getResources().getDrawable(R.drawable.btn_bg_sel));
                    join.setText("Join");
                    participa = false;
                }
                else{
                    //conectionAPI put participa
                    User.getInstance().addParticipa();
                    join.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                    join.setText("Leave");
                    participa = true;
                }
            }
        });

        participantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent participants = new Intent(EventActivity.this, ParticipantsActivity.class);
                //startActivity(participants);
            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void addCameraPosition() {
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(place.latitude(), place.longitude()))
                        .zoom(15)
                        .build()), 4000);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        addCameraPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        desc = extras.getString("desc");
        date = extras.getString("date");
        hour = extras.getString("hour");
        place = (Point) extras.get("place");
        id = extras.getInt("id");
        pos = extras.getInt("position");
        comunity = extras.getBoolean("comunity");
    }

}
