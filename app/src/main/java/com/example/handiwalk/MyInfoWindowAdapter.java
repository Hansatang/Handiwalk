package com.example.handiwalk;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

  private Context context;

  public MyInfoWindowAdapter(Context ctx) {
    context = ctx;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  @Override
  public View getInfoContents(Marker marker) {
    View view = ((Activity) context).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);

    LocationObject infoWindowData = (LocationObject) marker.getTag();
    TextView food_tv = view.findViewById(R.id.name);
    TextView transport_tv = view.findViewById(R.id.description);
    food_tv.setText(infoWindowData.getName());
    transport_tv.setText(infoWindowData.getDescription());

    return view;
  }
}