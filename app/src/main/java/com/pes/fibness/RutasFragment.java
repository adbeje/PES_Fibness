package com.pes.fibness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class RutasFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.lite_list_demo, container, false);

        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

        // Set up the RecyclerView
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new RutasFragment.MapAdapter(LIST_LOCATIONS));
        mRecyclerView.setRecyclerListener(mRecycleListener);
        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.layout_linear:
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                break;
            case R.id.layout_grid:
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                break;
        }
        return true;
    }

    /**
     * Adapter that displays a title and {@link com.google.android.gms.maps.MapView} for each item.
     * The layout is defined in <code>lite_list_demo_row.xml</code>. It contains a MapView
     * that is programatically initialised in
     * {@link #(int, android.view.View, android.view.ViewGroup)}
     */
    private class MapAdapter extends RecyclerView.Adapter<RutasFragment.MapAdapter.ViewHolder> {

        private RutasFragment.NamedLocation[] namedLocations;

        private MapAdapter(RutasFragment.NamedLocation[] locations) {
            super();
            namedLocations = locations;
        }

        @Override
        public RutasFragment.MapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RutasFragment.MapAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lite_list_demo_row, parent, false));
        }

        /**
         * This function is called when the user scrolls through the screen and a new item needs
         * to be shown. So we will need to bind the holder with the details of the next item.
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder == null) {
                return;
            }
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            return namedLocations.length;
        }

        /**
         * Holder for Views used in the {@link LiteListDemoActivity.MapAdapter}.
         * Once the  the <code>map</code> field is set, otherwise it is null.
         * When the {@link #onMapReady(com.google.android.gms.maps.GoogleMap)} callback is received and
         * the {@link com.google.android.gms.maps.GoogleMap} is ready, it stored in the {@link #map}
         * field. The map is then initialised with the NamedLocation that is stored as the tag of the
         * MapView. This ensures that the map is initialised with the latest data that it should
         * display.
         */
        class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

            MapView mapView;
            TextView title;
            GoogleMap map;
            View layout;

            private ViewHolder(View itemView) {
                super(itemView);
                layout = itemView;
                mapView = layout.findViewById(R.id.lite_listrow_map);
                title = layout.findViewById(R.id.lite_listrow_text);
                if (mapView != null) {
                    // Initialise the MapView
                    mapView.onCreate(null);
                    // Set the map ready callback to receive the GoogleMap object
                    mapView.getMapAsync(this);
                }
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getContext());
                map = googleMap;
                setMapLocation();
            }

            /**
             * Displays a {@link LiteListDemoActivity.NamedLocation} on a
             * {@link com.google.android.gms.maps.GoogleMap}.
             * Adds a marker and centers the camera on the NamedLocation with the normal map type.
             */
            private void setMapLocation() {
                if (map == null) return;

                RutasFragment.NamedLocation data = (RutasFragment.NamedLocation) mapView.getTag();
                if (data == null) return;

                // Add a marker for this item and set the camera
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(data.location, 13f));
                //map.addMarker(new MarkerOptions().position(data.location));
                map.addPolyline(new PolylineOptions()
                        .add(data.location)
                        .add(new LatLng(37.45, -122.0)));

                // Set the map type back to normal.
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }

            private void bindView(int pos) {
                RutasFragment.NamedLocation item = namedLocations[pos];
                // Store a reference of the ViewHolder object in the layout.
                layout.setTag(this);
                // Store a reference to the item in the mapView's tag. We use it to get the
                // coordinate of a location, when setting the map location.
                mapView.setTag(item);
                setMapLocation();
                title.setText(item.name);
            }
        }
    }

    /**
     * RecycleListener that completely clears the {@link com.google.android.gms.maps.GoogleMap}
     * attached to a row in the RecyclerView.
     * Sets the map type to {@link com.google.android.gms.maps.GoogleMap#MAP_TYPE_NONE} and clears
     * the map.
     */
    private RecyclerView.RecyclerListener mRecycleListener = new RecyclerView.RecyclerListener() {

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            RutasFragment.MapAdapter.ViewHolder mapHolder = (RutasFragment.MapAdapter.ViewHolder) holder;
            if (mapHolder != null && mapHolder.map != null) {
                // Clear the map and free up resources by changing the map type to none.
                // Also reset the map when it gets reattached to layout, so the previous map would
                // not be displayed.
                mapHolder.map.clear();
                mapHolder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        }
    };

    /**
     * Location represented by a position ({@link com.google.android.gms.maps.model.LatLng} and a
     * name ({@link java.lang.String}).
     */
    private static class NamedLocation {

        public final String name;
        public final LatLng location;

        NamedLocation(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }
    }

    /**
     * A list of locations to show in this ListView.
     */
    private static final RutasFragment.NamedLocation[] LIST_LOCATIONS = new RutasFragment.NamedLocation[]{
            new RutasFragment.NamedLocation("Cape Town", new LatLng(-33.920455, 18.466941)),
            new RutasFragment.NamedLocation("Beijing", new LatLng(39.937795, 116.387224)),
            new RutasFragment.NamedLocation("Bern", new LatLng(46.948020, 7.448206)),
            new RutasFragment.NamedLocation("Breda", new LatLng(51.589256, 4.774396)),
            new RutasFragment.NamedLocation("Brussels", new LatLng(50.854509, 4.376678)),
            new RutasFragment.NamedLocation("Copenhagen", new LatLng(55.679423, 12.577114)),
            new RutasFragment.NamedLocation("Hannover", new LatLng(52.372026, 9.735672)),
            new RutasFragment.NamedLocation("Helsinki", new LatLng(60.169653, 24.939480)),
            new RutasFragment.NamedLocation("Hong Kong", new LatLng(22.325862, 114.165532)),
    };


}
