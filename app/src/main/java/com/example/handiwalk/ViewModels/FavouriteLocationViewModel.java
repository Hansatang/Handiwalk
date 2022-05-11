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

  public void addFavourite(LocationModel locationModel) {

  }


}
