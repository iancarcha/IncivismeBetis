package com.example.incivismebetis;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import com.example.incivismebetis.MainActivity;
import com.example.incivismebetis.R;

import com.example.incivismebetis.SharedViewModel;
import com.example.incivismebetis.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapFragment extends Fragment {
    private SharedViewModel shareViewModel;
    private FragmentMapBinding binding;
    public MapFragment() {

    }


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        shareViewModel  = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance().getReference();

        if (auth.getUid() != null) {
            DatabaseReference users = base.child("users");
            DatabaseReference uid = users.child(auth.getUid());
            DatabaseReference incidencies = uid.child("incidencies");
            SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);



            mapFragment.getMapAsync(map -> {
                map.setMyLocationEnabled(true);
                MutableLiveData<LatLng> currentLatLng = model.getCurrentLatLng();
                LifecycleOwner owner = getViewLifecycleOwner();
                currentLatLng.observe(owner, latLng -> {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    map.animateCamera(cameraUpdate);
                    currentLatLng.removeObservers(owner);

                    incidencies.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);

                            LatLng aux = new LatLng(
                                    Double.valueOf(incidencia.getLatitud()),
                                    Double.valueOf(incidencia.getLongitud())
                            );

                            IncidenciesInfoWindowAdapter customInfoWindow = new IncidenciesInfoWindowAdapter(
                                    getActivity()
                            );

                            Marker marker = map.addMarker(new MarkerOptions()
                                    .title(incidencia.getProblema())
                                    .snippet(incidencia.getDireccio())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .position(aux));
                            marker.setTag(incidencia);
                            map.setInfoWindowAdapter(customInfoWindow);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                });
            });



        }
        return view;
    }

}

