package com.pes.fibness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.Point;

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
        RutasFragment.MapAdapter adapter = new RutasFragment.MapAdapter(LIST_LOCATIONS);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapPage = new Intent(getActivity(), MapViewActivity.class);
                int pos = mRecyclerView.getChildLayoutPosition(view);
                Toast.makeText(view.getContext(), "" + pos, Toast.LENGTH_SHORT).show();
                mapPage.putExtra("origen", LIST_LOCATIONS[pos].origen);
                mapPage.putExtra("destino", LIST_LOCATIONS[pos].destino);
                mapPage.putExtra("tituloRuta", LIST_LOCATIONS[pos].name);
                startActivity(mapPage);
            }
        });
        mRecyclerView.setAdapter(adapter);
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
    private class MapAdapter extends RecyclerView.Adapter<RutasFragment.MapAdapter.ViewHolder> implements View.OnClickListener{

        private RutasFragment.NamedLocation[] namedLocations;
        private View.OnClickListener listener;

        private MapAdapter(RutasFragment.NamedLocation[] locations) {
            super();
            namedLocations = locations;
        }

        @NonNull
        @Override
        public RutasFragment.MapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lite_list_demo_row, parent, false);

            view.setOnClickListener(this);

            return new RutasFragment.MapAdapter.ViewHolder(view);
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

        public void setOnClickListener(View.OnClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onClick(v);
            }
        }

        /**
         * Holder for Views used in the {@link RutasFragment.MapAdapter}.
         * Once the  the <code>map</code> field is set, otherwise it is null.
         *
         * the {@link com.google.android.gms.maps.GoogleMap} is ready, it stored in the {@link #map}
         * field. The map is then initialised with the NamedLocation that is stored as the tag of the
         * MapView. This ensures that the map is initialised with the latest data that it should
         * display.
         */
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView mapView;
            TextView title;
            MapboxStaticMap map;
            View layout;

            private ViewHolder(View itemView) {
                super(itemView);
                layout = itemView;
                mapView = layout.findViewById(R.id.lite_listrow_map);
                title = layout.findViewById(R.id.lite_listrow_text);
            }

            /**
             * Displays a {@link RutasFragment.NamedLocation} on a
             * {@link com.google.android.gms.maps.GoogleMap}.
             * Adds a marker and centers the camera on the NamedLocation with the normal map type.
             */
            private void setMapLocation() {

                RutasFragment.NamedLocation data = (RutasFragment.NamedLocation) mapView.getTag();
                com.google.android.gms.maps.model.LatLng center = LatLngBounds.builder()
                        .include(new com.google.android.gms.maps.model.LatLng(data.origen.latitude(), data.origen.longitude()))
                        .include(new com.google.android.gms.maps.model.LatLng(data.destino.latitude(), data.destino.longitude()))
                        .build()
                        .getCenter();
                map = MapboxStaticMap.builder()
                        .accessToken(getString(R.string.mapBox_ACCESS_TOKEN))
                        .styleId(StaticMapCriteria.STREET_STYLE)
                        .cameraPoint( Point.fromLngLat(center.longitude, center.latitude)) // Image's centerpoint on map
                        .cameraZoom(13)
                        .width(320) // Image width
                        .height(150) // Image height
                        .retina(true) // Retina 2x image will be returned
                        .build();
                String imageUrl = map.url().toString();

                Glide.with(RutasFragment.this)
                        .load(imageUrl)
                        .centerCrop()
                        .into(mapView);
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

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Toast.makeText(v.getContext(),Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent mapPage = new Intent(getActivity(), MapViewActivity.class);
                startActivity(mapPage);
            }
        }
    }

    /**
     * Location represented by a position ({@link com.google.android.gms.maps.model.LatLng} and a
     * name ({@link java.lang.String}).
     */
    private static class NamedLocation {

        public final String name;
        private final Point origen;
        private final Point destino;

        NamedLocation(String name, Point orig, Point dest) {
            this.name = name;
            this.origen = orig;
            this.destino = dest;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * A list of locations to show in this ListView.
     */
    private static final RutasFragment.NamedLocation[] LIST_LOCATIONS = new RutasFragment.NamedLocation[]{
            new RutasFragment.NamedLocation("Ruta Granada", Point.fromLngLat(-3.588098, 37.176164), Point.fromLngLat(-3.601845, 37.184080)),
            new RutasFragment.NamedLocation("De casa al insti", Point.fromLngLat(2.093580, 41.322220), Point.fromLngLat(2.102213, 41.330124)),
            new RutasFragment.NamedLocation("Ruta Granada", Point.fromLngLat(-3.588098, 37.176164), Point.fromLngLat(-3.601845, 37.184080)),
            new RutasFragment.NamedLocation("Ruta Granada", Point.fromLngLat(-3.588098, 37.176164), Point.fromLngLat(-3.601845, 37.184080))
    };


}
