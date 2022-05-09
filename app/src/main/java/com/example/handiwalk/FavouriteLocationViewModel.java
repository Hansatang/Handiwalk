package com.example.handiwalk;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteLocationViewModel extends AndroidViewModel {

    private final FavouriteRepository favouriteRepository;

    public FavouriteLocationViewModel(Application app) {
        super(app);
        favouriteRepository = FavouriteRepository.getInstance(app);
    }

    public LiveData<List<LocationObject>> init() {
        return favouriteRepository.getLocationLiveData();
    }



}
