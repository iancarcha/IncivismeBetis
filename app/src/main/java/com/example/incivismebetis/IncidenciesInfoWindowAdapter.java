package com.example.incivismebetis;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class IncidenciesInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Activity activity;

    public IncidenciesInfoWindowAdapter(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;

    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view = activity.getLayoutInflater()
                .inflate(R.layout.info_view, null);

        Incidencia incidencia = (Incidencia) marker.getTag();

       // ImageView ivProblema = view.findViewById(R.id.iv_problema);
        //TextView tvProblema = view.findViewById(R.id.tvProblema);
        //TextView tvDescripcio = view.findViewById(R.id.Descripcio);

        //tvProblema.setText(incidencia.getProblema());
        //tvDescripcio.setText(incidencia.getDireccio());

        return view;
    }

}
