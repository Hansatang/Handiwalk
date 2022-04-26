package com.example.handiwalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

public class MapFrag extends Fragment implements OnMapReadyCallback,ListenerChooseMap {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_lay, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng VIA = new LatLng(55.863838, 9.86122);
        mMap.addMarker(new MarkerOptions()
                .position(VIA)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(VIA));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VIA, 15));
        // Zoom in, animating the camera.

        LatLng AarsSkov = new LatLng(56.821938234395894, 9.501755476978627);
        mMap.addMarker(new MarkerOptions()
                .position(AarsSkov)
                .title("The forrest in Aars"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(AarsSkov));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AarsSkov, 15));
        // Zoom in, animating the camera.

    }

    @Override
    public void setCoordinates(GeoPoint coordinates) {
        
    }
}