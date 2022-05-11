package com.example.handiwalk.Repositories;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.handiwalk.Models.LocationModel;
import com.example.handiwalk.Models.ReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingRepository {

  private final MutableLiveData<List<ReviewModel>> reviewLiveData;
  private final MutableLiveData<ReviewModel> snapLiveData;


  private static RatingRepository instance;

  private RatingRepository(Application application) {

    this.reviewLiveData = new MutableLiveData<>();
    this.snapLiveData = new MutableLiveData<>();
  }

  public static synchronized RatingRepository getInstance(Application application) {
    if (instance == null)
      instance = new RatingRepository(application);
    return instance;
  }



  public void setRating(LocationModel locationModel, float rating) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference = db.collection("locations").document(locationModel.getId() + "");
    reference.update("Reviews", FieldValue.arrayUnion(String.valueOf(rating))).addOnCompleteListener(arrayUnionTask -> {
      reference.get().addOnCompleteListener(getDocumentTask -> {
        DocumentSnapshot document = getDocumentTask.getResult();
        ArrayList<String> reviews = (ArrayList<String>) document.getData().get("Reviews");
        String average = calculateAverage(reviews);
        System.out.println("Average : " + average);
        reference.update("AverageRating", average).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d(TAG, "DocumentSnapshot successfully updated!");
          }
        })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
              }
            });
      });
    });
  }


  private String calculateAverage(ArrayList<String> marks) {
    if (marks == null || marks.isEmpty()) {
      return "0";
    }
    System.out.println("Size " + marks.size());
    double sum = 0;
    for (String mark : marks) {
      double rate = Double.parseDouble(mark);
      sum += rate;
    }
    return String.valueOf(sum / marks.size());

  }

  public LiveData<List<ReviewModel>> getReviewLiveData() {
    return reviewLiveData;
  }

  public void setSnap(ReviewModel clickedItemIndex) {
    snapLiveData.setValue(clickedItemIndex);
  }

  public MutableLiveData<ReviewModel> getSnapLiveData() {
    return snapLiveData;
  }

}
