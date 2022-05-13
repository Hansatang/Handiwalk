package com.example.handiwalk.Repositories;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.handiwalk.Models.LocationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavouriteRepository {
  private static FavouriteRepository instance;
  private final MutableLiveData<List<LocationModel>> locationLiveData;
  private final MutableLiveData<Boolean> resultData;

  private FavouriteRepository(Application application) {
    locationLiveData = new MutableLiveData<>();
    resultData = new MutableLiveData<>();
    resultData.setValue(false);
  }

  public static synchronized FavouriteRepository getInstance(Application application) {
    if (instance == null)
      instance = new FavouriteRepository(application);
    return instance;
  }


  public LiveData<List<LocationModel>> getLocationLiveData() {
    return locationLiveData;
  }

  public void addFavourite(LocationModel newFavouriteLocation) {
    CollectionReference favourites = FirebaseFirestore.getInstance().collection("favourites");

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
          resultData.postValue(true);
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }


  public void getFavourites() {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("favourites").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

    docRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          ArrayList<Long> favs = (ArrayList<Long>) document.getData().get("favs");
          getFavouriteLocations(favs);
        } else {
          Log.d(TAG, "No such document");
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
          if (favs.contains(document.getData().get("Id"))) {
            LocationModel locationObject = new LocationModel(
                (String) document.getData().get("Name"),
                (GeoPoint) document.getData().get("Coordinates"),
                (String) document.getData().get("Description"),
                ((long) document.getData().get("Id")),
                true,
                (String) document.getData().get("AverageRating"));
            temp.add(locationObject);
          }
        }
        locationLiveData.setValue(temp);
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
      }
    });
  }

  public MutableLiveData<Boolean> getResultData() {
    return resultData;
  }

  public void clearResultData() {
    resultData.setValue(false);
  }

  public void deleteFavourite(LocationModel clickedItemIndex) {
    System.out.println("Int " + clickedItemIndex.getId());
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("favourites").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    docRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          ArrayList<Long> favs = (ArrayList<Long>) document.getData().get("favs");
          favs.remove(new Long(clickedItemIndex.getId()));
          docRef.update("favs", favs);
          resultData.postValue(true);
        } else {
          Log.d(TAG, "No such document");
        }
      } else {
        Log.d(TAG, "get failed with ", task.getException());
      }
    });
  }
}
