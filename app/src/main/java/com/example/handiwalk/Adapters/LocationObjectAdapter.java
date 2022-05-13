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

public class LocationObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    RecyclerView.ViewHolder viewHolder = null;
    if (viewType == 0) {
      view = inflater.inflate(R.layout.list_layout_default, parent, false);
      viewHolder = new ViewHolderDefault(view);
    } else {
      view = inflater.inflate(R.layout.list_favorite_layout, parent, false);
      viewHolder = new ViewHolderFav(view);
    }
    return viewHolder;
  }

  @Override
  public int getItemViewType(int position) {
    final LocationModel dataObj = objects.get(position);

    if (!dataObj.isFav()) {
      return 0;
    } else {
      return 1;
    }
  }

  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder.getItemViewType() == 0) {
      ViewHolderDefault vaultItemHolder = (ViewHolderDefault) viewHolder;
      vaultItemHolder.locationName.setText(objects.get(position).getName());
      double rate = Double.parseDouble(objects.get(position).getAverageRating());
      if (rate == 0) {
        vaultItemHolder.ratingScore.setText(R.string.NoRating);
      } else if (rate <= 1.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Poor", rate));
      } else if (rate <= 2.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Fair", rate));
      } else if (rate <= 3.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Good", rate));
      } else if (rate <= 4.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Very Good", rate));
      } else if (rate <= 5.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Excellent", rate));
      }
      vaultItemHolder.description.setText(objects.get(position).getDescription());
    }
    else{
      ViewHolderFav vaultItemHolder = (ViewHolderFav) viewHolder;
      vaultItemHolder.locationName.setText(objects.get(position).getName());
      double rate = Double.parseDouble(objects.get(position).getAverageRating());
      if (rate == 0) {
        vaultItemHolder.ratingScore.setText(R.string.NoRating);
      } else if (rate <= 1.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Poor", rate));
      } else if (rate <= 2.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Fair", rate));
      } else if (rate <= 3.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Good", rate));
      } else if (rate <= 4.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Very Good", rate));
      } else if (rate <= 5.0) {
        vaultItemHolder.ratingScore.setText(String.format("Rating: %s Excellent", rate));
      }
      vaultItemHolder.description.setText(objects.get(position).getDescription());
    }

  }


  public interface OnListItemClickListener {
    void onListItemClick(LocationModel clickedItemIndex);

    void onRateClick(LocationModel clickedItemIndex);

    void onFavClick(LocationModel clickedItemIndex);
  }

  public int getItemCount() {
    return objects.size();
  }

  class ViewHolderDefault extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView locationName;
    TextView ratingScore;
    TextView description;
    Button button;
    Button rateButton;
    Button favouriteButton;

    ViewHolderDefault(View itemView) {
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
      } else if (view.getId() == favouriteButton.getId()) {
        System.out.println(" FAV BUTTON ");
        clickListener.onFavClick(objects.get(getBindingAdapterPosition()));
      } else {
        clickListener.onListItemClick(objects.get(getBindingAdapterPosition()));
      }
    }
  }

  private class ViewHolderFav extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView locationName;
    TextView ratingScore;
    TextView description;
    Button button;
    Button rateButton;
    Button favouriteButton;

    public ViewHolderFav(View itemView) {
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
      } else if (view.getId() == favouriteButton.getId()) {
        System.out.println(" FAV BUTTON ");
        clickListener.onFavClick(objects.get(getBindingAdapterPosition()));
      } else {
        clickListener.onListItemClick(objects.get(getBindingAdapterPosition()));
      }
    }
  }

}
