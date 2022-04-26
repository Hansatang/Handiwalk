package com.example.handiwalk;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChooseViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;

    public ChooseViewModel(Application app) {
        super(app);

        locationRepository = LocationRepository.getInstance(app);
    }

    public LiveData<List<LocationObject>> init() {
        return locationRepository.getLocationLiveData();
    }
}
