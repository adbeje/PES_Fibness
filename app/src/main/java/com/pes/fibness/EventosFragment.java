package com.pes.fibness;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.Point;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;

public class EventosFragment extends Fragment{

    TextView mEvents, cEvents;
    RecyclerView listEvents;
    AdapterEventos adapterC;
    AdapterEventos adapterM;
    boolean comunity = true;
    ArrayList<Evento> mEventosList = new ArrayList<>();
    ArrayList<Evento> cEventosList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eventos, container, false);

        final FloatingActionButton newEvent = root.findViewById(R.id.fb_new_event);
        listEvents = root.findViewById(R.id.recyclerEventos);
        mEvents = root.findViewById(R.id.mEvents);
        cEvents = root.findViewById(R.id.cEvents);

        listEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //cEventosList = User.getInstance().getMyEventList();
        //mEventosList = User.getInstance().getEventList();
        for (int i = 0; i < 10; ++i){
            Evento e = new Evento();
            e.id = 0;
            e.hour = "20:00";
            e.desc = "NBA 2K20 tournament";
            e.name = "NBA 2K20 tournament";
            e.date = "02/06/2020";
            e.place = Point.fromLngLat(2.16,41.37);
            cEventosList.add(e);
        }
        Evento e = new Evento();
        e.id = 0;
        e.hour = "20:00";
        e.desc = "NBA 2K20 tournament";
        e.name = "NBA 2K20 tournament";
        e.date = "02/06/2020";
        e.place = Point.fromLngLat(0,0);
        mEventosList.add(e);
        adapterM = new AdapterEventos(mEventosList);
        adapterC = new AdapterEventos(cEventosList);

        adapterM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_event = new Intent(getActivity(), EventActivity.class);
                startActivity(view_event);
            }
        });

        adapterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_event = new Intent(getActivity(), EventActivity.class);
                startActivity(view_event);
            }
        });

        listEvents.setAdapter(adapterC);

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_event = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(create_event);
            }
        });

        mEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEvent.setVisibility(View.VISIBLE);
                newEvent.setClickable(true);
                mEvents.setTextColor(getResources().getColor(R.color.seleccion));
                cEvents.setTextColor(getResources().getColor(R.color.blanco));
                comunity = false;
                updateRecycler();
            }
        });

        cEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEvent.setVisibility(View.INVISIBLE);
                newEvent.setClickable(false);
                cEvents.setTextColor(getResources().getColor(R.color.seleccion));
                mEvents.setTextColor(getResources().getColor(R.color.blanco));
                comunity = true;
                updateRecycler();
            }
        });

        return root;

    }

    private void updateRecycler(){
        if(comunity){
            //cEventosList = User.getInstance().getMyEventList();
            adapterC.notifyDataSetChanged();
            listEvents.setAdapter(adapterC);
        }
        else{
            //mEventosList = User.getInstance().getEventList();
            adapterM.notifyDataSetChanged();
            listEvents.setAdapter(adapterM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //cEventosList = User.getInstance().getMyEventList();
        //mEventosList = User.getInstance().getEventList();
        adapterC.notifyDataSetChanged();
        adapterM.notifyDataSetChanged();
    }

}


class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.ViewHolderEvents> implements View.OnClickListener{

    private ArrayList<Evento> listaEvents;
    private View.OnClickListener listener;

    public AdapterEventos(ArrayList<Evento> listaEvents) {
        this.listaEvents = listaEvents;
    }

    @NotNull
    @Override
    public ViewHolderEvents onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_row, null, false);
        view.setOnClickListener(this);
        return new ViewHolderEvents(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvents holder, int position) {
        holder.assignEvents(listaEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return listaEvents.size();
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


    class ViewHolderEvents extends RecyclerView.ViewHolder {

        ImageView mapView;
        TextView title;
        TextView desc;
        TextView date;
        TextView hour;
        MapboxStaticMap map;
        View layout;

        public ViewHolderEvents(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            mapView = itemView.findViewById(R.id.event_map);
            title = itemView.findViewById(R.id.event_title);
            desc = itemView.findViewById(R.id.event_desc);
            date = itemView.findViewById(R.id.event_date);
            hour = itemView.findViewById(R.id.event_hour);
        }

        private void setMapLocation() {
            Point data = (Point) mapView.getTag();
            map = MapboxStaticMap.builder()
                    .accessToken("pk.eyJ1IjoiYWR2ZWhlIiwiYSI6ImNrOXgzZm80dzA3Zmwzc3FkMnN2ODQ4ZGcifQ.xu6iKxJMZDnzYvhtzCTzyw")
                    .styleId(StaticMapCriteria.STREET_STYLE)
                    .cameraPoint( Point.fromLngLat(data.longitude(), data.latitude()))
                    .cameraZoom(13)
                    .width(150) // Image width
                    .height(150) // Image height
                    .retina(true) // Retina 2x image will be returned
                    .build();
            String imageUrl = map.url().toString();

            Glide.with(layout)
                    .load(imageUrl)
                    .centerCrop()
                    .into(mapView);
        }

        public void assignEvents(Evento evento) {
            title.setText(evento.name);
            desc.setText(evento.desc);
            hour.setText(evento.hour);
            date.setText(evento.date);
            mapView.setTag(evento.place);
            setMapLocation();
        }
    }
}
