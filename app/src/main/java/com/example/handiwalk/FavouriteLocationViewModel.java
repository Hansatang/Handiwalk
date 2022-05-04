package com.example.handiwalk;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteLocationViewModel extends AndroidViewModel {

    private final LocalRepository localRepository;

    public FavouriteLocationViewModel(Application app) {
        super(app);

        localRepository = LocationRepository.getInstance(app);
    }

    public LiveData<List<LocationObject>> init() {
        return localRepository.getLocationLiveData();
    }

    public void setSnap(LocationObject clickedItemIndex) {
        localRepository.setSnap(clickedItemIndex);
    }

    public LiveData<LocationObject> snapInit() {
        return localRepository.getSnapLiveData();
    }

}
