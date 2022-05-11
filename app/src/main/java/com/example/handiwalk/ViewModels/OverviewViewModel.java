package com.example.handiwalk.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Repositories.LocationRepository;
import com.example.handiwalk.Repositories.RatingRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;
    private final RatingRepository ratingRepository;

    public OverviewViewModel(Application app) {
        super(app);

        locationRepository = LocationRepository.getInstance(app);
        ratingRepository = RatingRepository.getInstance(app);
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

    public void setReview(LocationModel reviewedLocation, float ratingValue){
        ratingRepository.setRating(reviewedLocation, ratingValue*10);
    }


}
