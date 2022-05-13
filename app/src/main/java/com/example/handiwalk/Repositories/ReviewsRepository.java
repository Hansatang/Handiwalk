package com.example.handiwalk.Repositories;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.handiwalk.Models.LocationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReviewsRepository {
  private static ReviewsRepository instance;
  private final MutableLiveData<Boolean> resultData;

  private ReviewsRepository(Application application) {
    resultData = new MutableLiveData<>();
    resultData.setValue(false);
  }

  public static synchronized ReviewsRepository getInstance(Application application) {
    if (instance == null)
      instance = new ReviewsRepository(application);
    return instance;
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
          resultData.postValue(true);
        }
    ).addOnFailureListener(e -> Log.w(TAG, "Error updating AverageRating", e));
  }

  public MutableLiveData<Boolean> getResultData() {
    return resultData;
  }

  public void clearResultData() {
    resultData.setValue(false);
  }
}
