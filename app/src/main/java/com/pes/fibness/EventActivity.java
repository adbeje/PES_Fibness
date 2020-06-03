package com.pes.fibness;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;

import java.util.List;
import java.util.Objects;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

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

    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapBox_ACCESS_TOKEN));
        setContentView(R.layout.activity_view_event);
        getExtras();

        ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/event/" + id + "/participants");
        connection.getParticipants();

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
                ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/event/" + id);
                connection.deleteEvent();
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
                    ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/event/" + id);
                    connection.deleteParticipa();
                    User.getInstance().deleteParticipa();
                    join.setBackground(getResources().getDrawable(R.drawable.btn_bg_sel));
                    join.setText("Join");
                    participa = false;
                }
                else{
                    ConnetionAPI connection = new ConnetionAPI(getApplicationContext(), "http://10.4.41.146:3001/event/" + id + "/join");
                    connection.createParticipa();
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

        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                addDestinationIconSymbolLayer(style);
                GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
                source.setGeoJson(Feature.fromGeometry(place));
            }
        });
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

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            //locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {

        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(Objects.requireNonNull(mapboxMap.getStyle()));
        } else {
            //Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
