package com.example.handiwalk;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteLocationViewModel extends AndroidViewModel {

    private final LocalRepository localRepository;

    public FavouriteLocationViewModel(Application app) {
        super(app);
        localRepository = LocalRepository.getInstance(app);
    }

    public LiveData<List<LocationObject>> init() {
        return localRepository.getLocationLiveData();
    }



}
