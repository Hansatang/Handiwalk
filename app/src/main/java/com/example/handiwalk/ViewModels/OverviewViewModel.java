package com.example.handiwalk.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Repositories.FavouriteRepository;
import com.example.handiwalk.Repositories.LocationRepository;
import com.example.handiwalk.Repositories.RatingRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
  private final LocationRepository locationRepository;
  private final RatingRepository ratingRepository;
  private final FavouriteRepository favouriteRepository;

  public OverviewViewModel(Application app) {
    super(app);

    locationRepository = LocationRepository.getInstance(app);
    ratingRepository = RatingRepository.getInstance(app);
    favouriteRepository = FavouriteRepository.getInstance(app);
  }

  public LiveData<List<LocationModel>> init() {
    return locationRepository.getLocationLiveData();
  }

  public void setSnap(LocationModel clickedItemIndex) {
    locationRepository.setSnap(clickedItemIndex);
  }

  public LiveData<LocationModel> snapInit() {
    return locationRepository.getSnapLiveData();
  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  public void setReview(LocationModel reviewedLocation, float ratingValue) {
    locationRepository.setRating(reviewedLocation, ratingValue);
  }

  public void addFav(LocationModel locationModel){
    favouriteRepository.addFavourite(locationModel);
  }


  public void populateLive() {
    locationRepository.getLocationsCoordinates();
  }
}
