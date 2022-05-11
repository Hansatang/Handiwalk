package com.example.handiwalk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.R;

import java.util.ArrayList;
import java.util.List;

public class LocationObjectAdapter extends RecyclerView.Adapter<LocationObjectAdapter.ViewHolder> {
  final private LocationObjectAdapter.OnListItemClickListener clickListener;
  private List<LocationModel> objects;

  public LocationObjectAdapter(LocationObjectAdapter.OnListItemClickListener listener) {
    objects = new ArrayList<>();
    clickListener = listener;
  }

  public void update(List<LocationModel> list) {
    System.out.println("Update call");
    if (list != null) {
      objects = list;
      notifyDataSetChanged();
    }
  }


  @NonNull
  public LocationObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    view = inflater.inflate(R.layout.list_layout, parent, false);
    return new LocationObjectAdapter.ViewHolder(view);
  }

  public void onBindViewHolder(LocationObjectAdapter.ViewHolder viewHolder, int position) {
    viewHolder.locationName.setText(objects.get(position).getName());
    double rate = Double.parseDouble(objects.get(position).getAverageRating());
    if (rate == 0) {
      viewHolder.ratingScore.setText(R.string.NoRating);
    } else if (rate <= 1.0) {
      viewHolder.ratingScore.setText(String.format("Rating: %s Poor", rate));
    } else if (rate <= 2.0) {
      viewHolder.ratingScore.setText(String.format("Rating: %s Fair", rate));
    } else if (rate <= 3.0) {
      viewHolder.ratingScore.setText(String.format("Rating: %s Good", rate));
    } else if (rate <= 4.0) {
      viewHolder.ratingScore.setText(String.format("Rating: %s Very Good", rate));
    } else if (rate <= 5.0) {
      viewHolder.ratingScore.setText(String.format("Rating: %s Excellent", rate));
    }
    viewHolder.description.setText(objects.get(position).getDescription());
  }


  public interface OnListItemClickListener {
    void onListItemClick(LocationModel clickedItemIndex);
    void onRateClick(LocationModel clickedItemIndex);

    void onFavClick(LocationModel clickedItemIndex);
  }

  public int getItemCount() {
    return objects.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView locationName;
    TextView ratingScore;
    TextView description;
    Button button;
    Button rateButton;
    Button favouriteButton;

    ViewHolder(View itemView) {
      super(itemView);
      locationName = itemView.findViewById(R.id.locationName);
      ratingScore = itemView.findViewById(R.id.ratingScore);
      description = itemView.findViewById(R.id.desciption);

      button = itemView.findViewById(R.id.showOnMap);
      rateButton = itemView.findViewById(R.id.rateButton);
      favouriteButton = itemView.findViewById(R.id.favButton);
      button.setOnClickListener(this);
      rateButton.setOnClickListener(this);
      favouriteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      System.out.println(view.getId());
      System.out.println(rateButton.getId());
      System.out.println(button.getId());
      if (view.getId() == rateButton.getId()) {
        clickListener.onRateClick(objects.get(getBindingAdapterPosition()));
      }
      if(view.getId() == favouriteButton.getId()){
        System.out.println(" FAV BUTTON ");
        clickListener.onFavClick(objects.get(getBindingAdapterPosition()));
      }

      else {
        clickListener.onListItemClick(objects.get(getBindingAdapterPosition()));
      }
    }
  }
}
