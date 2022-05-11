package com.example.handiwalk.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

  private Context context;

  public InfoWindowAdapter(Context ctx) {
    context = ctx;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  @Override
  public View getInfoContents(Marker marker) {
    View view = ((Activity) context).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);

    LocationModel infoWindowData = (LocationModel) marker.getTag();
    TextView food_tv = view.findViewById(R.id.name);
    TextView transport_tv = view.findViewById(R.id.description);
    food_tv.setText(infoWindowData.getName());
    transport_tv.setText(infoWindowData.getDescription());

    return view;
  }
}