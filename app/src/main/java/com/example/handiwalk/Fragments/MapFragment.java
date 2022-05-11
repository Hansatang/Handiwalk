package com.example.handiwalk.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.handiwalk.ViewModels.OverviewViewModel;
import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Adapters.InfoWindowAdapter;
import com.example.handiwalk.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    OverviewViewModel viewModel;
    View view;
    private GoogleMap mMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.map_lay, container, false);

        viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.859070694447674, 9.849398618961366), 6));
        viewModel.getLocations().observe(getViewLifecycleOwner(), this::updateMap);
        viewModel.snapInit().observe(getViewLifecycleOwner(), this::SnapToSelected);


    }

    private void updateMap(List<LocationModel> listObjects) {

        for (LocationModel temp : listObjects) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(temp.getCoordinates().getLatitude(), temp.getCoordinates().getLongitude()))
                .title(temp.getName())
            .snippet(temp.getDescription()));
            marker.setTag(temp);
        }

    }


    private void SnapToSelected(LocationModel object) {

        if (object != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(object.getCoordinates().getLatitude(), object.getCoordinates().getLongitude()), 15));
            viewModel.setSnap(null);
        }

    }

}