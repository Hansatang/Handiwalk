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
          Log.d(TAG, "No such documen favoruitet");
        }
      } else {
        Log.d(TAG, "get failed with ", task.getException());
      }
    });

  }

  private void getFavouriteLocations(ArrayList<Long> favs) {
    List<LocationModel> temp = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("locations").get().addOnCompleteListener(task -> {
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


  public void setRating(LocationModel locationModel, String rating) {
    DocumentReference reference = FirebaseFirestore.getInstance().
        collection("locations").document(locationModel.getId() + "");

    String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    reference.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        ArrayList<Map<String, Object>> maps = (ArrayList<Map<String, Object>>) task.getResult().getData().get("Reviews");
        String average = prepareArrayOfMaps(rating, userUid, Objects.requireNonNull(maps));
        setAverageRatingAndReviews(reference, maps, average);
        //  Log.d(TAG, document.getId() + " => " + document.getData());
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
      }
    });
  }



  @NonNull
  private String prepareArrayOfMaps(String rating, String userUid, ArrayList<Map<String, Object>> maps) {
    ArrayList<String> reviews = new ArrayList<>();
    boolean addNew = true;

    for (Map<String, Object> map : maps) {
      if (map.containsKey(userUid)) {
        addNew = false;
        map.replace(userUid, String.valueOf(rating));
        map.values().forEach(tab -> reviews.add((String) tab));
      } else {
        map.values().forEach(tab -> reviews.add((String) tab));
      }
    }

    if (addNew) {
      Map<String, Object> docData = new HashMap<>();
      docData.put(userUid, String.valueOf(rating));
      maps.add(docData);
      reviews.add(String.valueOf(rating));
    }
    return calculateAverage(reviews);
  }


  private String calculateAverage(ArrayList<String> marks) {
    double sum = 0;
    for (String mark : marks) {
      double rate = Double.parseDouble(mark);
      sum += rate;
    }
    double result = Math.round(((sum / marks.size()) * 100.0)) / 100.0;
    return String.valueOf(result);
  }


  private void setAverageRatingAndReviews(DocumentReference reference, ArrayList<Map<String, Object>> maps, String average) {
    reference.update("Reviews", maps).addOnSuccessListener(aVoid -> {
          Log.d(TAG, "Reviews successfully updated!");
          setAverageRating(reference, average);
        }
    ).addOnFailureListener(e -> Log.w(TAG, "Error updating Reviews", e));
  }

  private void setAverageRating(DocumentReference reference, String average) {
    reference.update("AverageRating", average).addOnSuccessListener(aVoid -> {
          Log.d(TAG, "AverageRating successfully updated!");
          getFavourites();
        }
    ).addOnFailureListener(e -> Log.w(TAG, "Error updating AverageRating", e));
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

  public void addFavourite(LocationModel newFavouriteLocation) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference favourites = db.collection("favourites");

    DocumentReference docRef = favourites.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            docRef.update("favs", FieldValue.arrayUnion((long) newFavouriteLocation.getId())).addOnCompleteListener(arrayUnionTask -> {
              docRef.get().addOnCompleteListener(getDocumentTask -> {
                System.out.println("Added " + newFavouriteLocation.getName() + " to favourites");
              });
            });
          } else {
            Log.d(TAG, "No such document");
            Map<String, Object> data = new HashMap<>();
            List<Long> favs = new ArrayList<>();
            favs.add(1L);
            data.put("favs", favs);
            favourites.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data);
            Log.d(TAG, "Created document for user.");

          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }
}