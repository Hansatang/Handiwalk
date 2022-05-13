package com.example.handiwalk.Repositories;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.handiwalk.Models.LocationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocationRepository {

  private static LocationRepository instance;
  private final MutableLiveData<List<LocationModel>> locationLiveData;
  private final MutableLiveData<LocationModel> snapLiveData;

  private LocationRepository(Application application) {
    locationLiveData = new MutableLiveData<>();
    snapLiveData = new MutableLiveData<>();
  }

  public static synchronized LocationRepository getInstance(Application application) {
    if (instance == null)
      instance = new LocationRepository(application);
    return instance;
  }

  public LiveData<List<LocationModel>> getLocationLiveData() {
    return locationLiveData;
  }

  public void setSnap(LocationModel clickedItemIndex) {
    snapLiveData.setValue(clickedItemIndex);
  }

  public MutableLiveData<LocationModel> getSnapLiveData() {
    return snapLiveData;
  }

  public void getFavourites() {

    DocumentReference docRef = FirebaseFirestore.getInstance().collection("favourites").
        document(FirebaseAuth.getInstance().getCurrentUser().getUid());

    docRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          ArrayList<Long> favs = (ArrayList<Long>) document.getData().get("favs");
          getFavouriteLocations(favs);
        } else {
          getLocationsCoordinates();
          Log.d(TAG, "No such document");
        }
      } else {
        Log.d(TAG, "get failed with ", task.getException());
      }
    });
  }

  private void getFavouriteLocations(ArrayList<Long> favs) {
    List<LocationModel> temp = new ArrayList<>();
    FirebaseFirestore.getInstance().collection("locations").get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              LocationModel locationObject = new LocationModel(
                  (String) document.getData().get("Name"),
                  (GeoPoint) document.getData().get("Coordinates"),
                  (String) document.getData().get("Description"),
                  ((long) document.getData().get("Id")),
                  false,
                  (String) document.getData().get("AverageRating"));
              if (favs.contains(document.getData().get("Id"))) {
                locationObject.setFav(true);
              }
              temp.add(locationObject);
            }
            locationLiveData.setValue(temp);
          } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
          }
        });
  }


  public void getLocationsCoordinates() {
    CollectionReference reference = FirebaseFirestore.getInstance().collection("locations");
    List<LocationModel> temp = new ArrayList<>();
    reference.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        for (QueryDocumentSnapshot document : task.getResult()) {
          LocationModel locationObject = new LocationModel(
              (String) document.getData().get("Name"),
              (GeoPoint) document.getData().get("Coordinates"),
              (String) document.getData().get("Description"),
              ((long) document.getData().get("Id")),
              false,
              (String) document.getData().get("AverageRating"));

          temp.add(locationObject);
          Log.d(TAG, document.getId() + " => " + document.getData());
        }
        locationLiveData.setValue(temp);
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
      }
    });
  }

}