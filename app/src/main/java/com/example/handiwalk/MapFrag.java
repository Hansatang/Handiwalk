package com.example.handiwalk;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MapFrag extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ChooseViewModel viewModel;
    View view;
    private GoogleMap mMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.map_lay, container, false);

        viewModel = new ViewModelProvider(this).get(ChooseViewModel.class);


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.859070694447674, 9.849398618961366), 6));
        viewModel.init().observe(getViewLifecycleOwner(), listObjects -> updateMap(listObjects));
        viewModel.snapInit().observe(getViewLifecycleOwner(), object -> SnapToSelected(object));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                System.out.println("aleooooo");
                // Retrieve the data from the marker.
                //LocationObject clickCount = (LocationObject) marker.getTag();

                // Check if a click count was set, then display the click count.

                LocationObject clickCount = (LocationObject) marker.getTag();

                // Check if a click count was set, then display the click count.
                if (clickCount != null) {

                    LayoutInflater inflater = (LayoutInflater)
                            getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.description_window, null);
                  TextView text= popupView.findViewById(R.id.description_text);
                    text.setText(((LocationObject) marker.getTag()).getName());
                    // create the popup window
                    int width = 680;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.LEFT, 10, 75);

                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });



                }
                    return false;

            }
        });

    }

    private void updateMap(List<LocationObject> listObjects) {

        for (LocationObject temp : listObjects) {

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(temp.getCoordinates().getLatitude(), temp.getCoordinates().getLongitude())).title(temp.getName()));
            marker.setTag(temp);
        }

    }


    private void SnapToSelected(LocationObject object) {

        if (object != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(object.getCoordinates().getLatitude(), object.getCoordinates().getLongitude()), 15));
            viewModel.setSnap(null);
        }

    }

}