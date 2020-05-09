package com.pes.fibness;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.LatLngBounds;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapViewActivity extends AppCompatActivity {


    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    private MapView mapView;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    private Point origin;
    private Point destination;
    private String tituloRuta;
    private String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapBox_ACCESS_TOKEN));
        setContentView(R.layout.activity_map_rutas);
        getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tituloRuta);
        getSupportActionBar().setSubtitle(descripcion);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(origin.latitude(), origin.latitude()))
                        .zoom(14)
                        .tilt(20)
                        .build();

                mapboxMap.setCameraPosition(position);
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        com.google.android.gms.maps.model.LatLng center = LatLngBounds.builder()
                                .include(new com.google.android.gms.maps.model.LatLng(origin.latitude(), origin.longitude()))
                                .include(new com.google.android.gms.maps.model.LatLng(destination.latitude(), destination.longitude()))
                                .build()
                                .getCenter();

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(center.latitude, center.longitude))
                                .zoom(14)
                                .build();

                        mapboxMap.setCameraPosition(position);

                        initSource(style);

                        initLayers(style);

                        // Get the directions route from the Mapbox Directions API
                        getRoute(mapboxMap, origin, destination);
                    }
                });
            }
        });
    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#15202B"))
        );
        loadedMapStyle.addLayer(routeLayer);

        // Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.blue_marker))));

        // Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -9f})));
    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     * @param mapboxMap the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private void getRoute(final MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapBox_ACCESS_TOKEN))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Timber.d("Response code: %s", response.code());
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No routes found");
                    return;
                }

                // Get the directions route
                currentRoute = response.body().routes().get(0);

                // Make a toast which displays the route's distance
                Toast.makeText(MapViewActivity.this, getString(R.string.directions_activity_toast_message) +
                currentRoute.distance(), Toast.LENGTH_SHORT).show();

                TextView dist = (TextView) findViewById(R.id.map_dist);
                dist.setText("Distancia: " + currentRoute.distance().intValue() + " m");

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Retrieve and update the source designated for showing the directions route
                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                            // Create a LineString with the directions route's geometry and
                            // reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(Objects.requireNonNull(currentRoute.geometry()), PRECISION_6));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
                Timber.e("Error: %s", throwable.getMessage());
                Toast.makeText(MapViewActivity.this, "Error: " + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// Cancel the Directions API request
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origin = (Point) extras.get("origen");
            destination = (Point)extras.get("destino");
            tituloRuta = extras.getString("tituloRuta");
            descripcion = extras.getString("descripcion");
        }

    }

}
