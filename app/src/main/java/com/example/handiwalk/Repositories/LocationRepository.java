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

  public void getLocations() {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference docRef = db.collection("locations").document("1");
//        CollectionReference col = db.collection("locations");
//        col.whereArrayContains("userId", "Test").get();
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "Name: " + document.getData());
            List<LocationModel> list = new ArrayList();
            System.out.println("NOme " + (String) document.getData().get("Desciption"));
            LocationModel locationObject = new LocationModel((String) document.getData().get("Name"),
                (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"),
                (long) document.getData().get("Id"),
                (String) document.getData().get("AverageRating"));

            list.add(locationObject);
            locationLiveData.setValue(list);
          } else {
            Log.d(TAG, "No such document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }


  public void getLocationsCoordinates() {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<LocationModel> temp = new ArrayList<>();
    db.collection("locations").get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        for (QueryDocumentSnapshot document : task.getResult()) {
          System.out.println(document.getData().get("Coordinates") + "");
          LocationModel locationObject;
          locationObject = new LocationModel((String) document.getData().get("Name"),
              (GeoPoint) document.getData().get("Coordinates"),
              (String) document.getData().get("Description"),
              ((long) document.getData().get("Id")),
              (String) document.getData().get("AverageRating"));

          temp.add(locationObject);
          Log.d(TAG, document.getId() + " => " + document.getData());
        }
        System.out.println("Amount: " + temp.size());
        locationLiveData.setValue(temp);
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void setRating(LocationModel locationModel, float rating) {
    System.out.println("Test 2");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference reference = db.collection("locations").document(locationModel.getId() + "");
    Map<String, Object> docData = new HashMap<>();
    docData.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), String.valueOf(rating));

    reference.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        ArrayList<Map<String, Object>> maps = (ArrayList<Map<String, Object>>) document.getData().get("Reviews");
        ArrayList<String> reviews = new ArrayList<>();
        for (Map map : maps) {
          if (map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            map.replace(FirebaseAuth.getInstance().getCurrentUser().getUid(), String.valueOf(rating));
            map.values().forEach(tab -> reviews.add((String) tab));
          } else {
            map.values().forEach(tab -> reviews.add((String) tab));
          }
        }
        String average = calculateAverage(reviews);
        reference.update("Reviews", maps).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d(TAG, "DocumentSnapshot successfully updated!");
            reference.update("AverageRating", average).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
                getLocationsCoordinates();
              }
            })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                  }
                });
          }
        }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
              }
            });
        Log.d(TAG, document.getId() + " => " + document.getData());
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
      }
    });


//    reference.update("Reviews", FieldValue.arrayUnion(docData)).addOnCompleteListener(arrayUnionTask -> {
//      reference.get().addOnCompleteListener(getDocumentTask -> {
//        DocumentSnapshot document = getDocumentTask.getResult();
//        ArrayList<Map<String, Object>> maps = (ArrayList<Map<String, Object>>) document.getData().get("Reviews");
//        ArrayList<String> reviews = new ArrayList<>();
//        for (Map map : maps) {
//          map.values().forEach(tab -> reviews.add((String) tab));
//        }
//        String average = calculateAverage(reviews);
//        System.out.println("Average : " + average);
//        reference.update("AverageRating", average).addOnSuccessListener(new OnSuccessListener<Void>() {
//          @Override
//          public void onSuccess(Void aVoid) {
//            Log.d(TAG, "DocumentSnapshot successfully updated!");
//            getLocationsCoordinates();
//          }
//        })
//            .addOnFailureListener(new OnFailureListener() {
//              @Override
//              public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error updating document", e);
//              }
//            });
//      });
//    });
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
    double result = Math.round(((sum / marks.size()) * 100.0)) / 100.0;

    return String.valueOf(result);

  }

  public void setSnap(LocationModel clickedItemIndex) {
    snapLiveData.setValue(clickedItemIndex);
  }

  public MutableLiveData<LocationModel> getSnapLiveData() {
    return snapLiveData;
  }
}