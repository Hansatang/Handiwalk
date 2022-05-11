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

    LocationModel infoWindowContent = (LocationModel) marker.getTag();
    TextView name = view.findViewById(R.id.name);
    TextView description = view.findViewById(R.id.description);
    TextView rate = view.findViewById(R.id.ratingInfo);
    name.setText(infoWindowContent.getName());
    description.setText(infoWindowContent.getDescription());
    rate.setText(infoWindowContent.getAverageRating() + "");

    return view;
  }
}