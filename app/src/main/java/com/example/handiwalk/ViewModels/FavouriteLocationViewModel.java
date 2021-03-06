package com.example.handiwalk.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.handiwalk.Repositories.FavouriteRepository;
import com.example.handiwalk.Models.LocationModel;

import java.util.List;

public class FavouriteLocationViewModel extends AndroidViewModel {

  private final FavouriteRepository favouriteRepository;

  public FavouriteLocationViewModel(Application app) {
    super(app);
    favouriteRepository = FavouriteRepository.getInstance(app);
  }

  public LiveData<List<LocationModel>> init() {
    return favouriteRepository.getLocationLiveData();
  }

  public void getFavourite() {
    favouriteRepository.getFavourites();
  }

  public LiveData<Boolean> getResult() {
    return favouriteRepository.getResultData();
  }

  public void addFav(LocationModel locationModel) {
    favouriteRepository.addFavourite(locationModel);
  }

  public void clearResult() {
    favouriteRepository.clearResultData();
  }

  public void deleteFav(LocationModel clickedItemIndex) {
    favouriteRepository.deleteFavourite(clickedItemIndex);
  }
}
