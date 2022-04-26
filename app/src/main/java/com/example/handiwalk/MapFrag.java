package com.example.handiwalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MapFrag extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ChooseViewModel viewModel;
    private GoogleMap mMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_lay, container, false);

        viewModel = new ViewModelProvider(this).get(ChooseViewModel.class);


        viewModel.init().observe(getViewLifecycleOwner(), listObjects -> updateMap(listObjects));

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.859070694447674, 9.849398618961366),6));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(AarsSkov));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AarsSkov, 15));

    }

    private void updateMap(List<LocationObject> listObjects){

        for (LocationObject temp:listObjects) {

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(temp.getCoordinates().getLatitude(),temp.getCoordinates().getLongitude())).title(temp.getName()));
        }
    }


}