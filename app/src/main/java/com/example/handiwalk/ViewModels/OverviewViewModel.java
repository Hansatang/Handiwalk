package com.example.handiwalk.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Repositories.FavouriteRepository;
import com.example.handiwalk.Repositories.LocationRepository;
import com.example.handiwalk.Repositories.ReviewsRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
  private final LocationRepository locationRepository;
  private final ReviewsRepository reviewsRepository;
  private final FavouriteRepository favouriteRepository;

  public OverviewViewModel(Application app) {
    super(app);

    locationRepository = LocationRepository.getInstance(app);
    reviewsRepository = ReviewsRepository.getInstance(app);
    favouriteRepository = FavouriteRepository.getInstance(app);
  }

  public LiveData<List<LocationModel>> getLocations() {
    return locationRepository.getLocationLiveData();
  }

  public void setSnap(LocationModel clickedItemIndex) {
    locationRepository.setSnap(clickedItemIndex);
  }

  public LiveData<LocationModel> snapInit() {
    return locationRepository.getSnapLiveData();
  }

  public void setReview(LocationModel reviewedLocation, float ratingValue) {
    reviewsRepository.setRating(reviewedLocation, String.valueOf(ratingValue));
  }

  public LiveData<Boolean> getResult() {
    return reviewsRepository.getResultData();
  }

  public void clearResult() {
    reviewsRepository.clearResultData();
  }

  public void init() {
    locationRepository.getFavourites();
  }


}
