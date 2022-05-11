package com.example.handiwalk.Repositories;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.handiwalk.Models.LocationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRepository {
    private static FavouriteRepository instance;
    private final MutableLiveData<List<LocationModel>> locationLiveData;
    private final MutableLiveData<LocationModel> snapLiveData;

    private FavouriteRepository(Application application) {
        locationLiveData = new MutableLiveData<>();
        snapLiveData = new MutableLiveData<>();
        getLocationsCoordinates();
    }

    public static synchronized FavouriteRepository getInstance(Application application) {
        if (instance == null)
            instance = new FavouriteRepository(application);
        return instance;
    }


    public LiveData<List<LocationModel>> getLocationLiveData() {
        return locationLiveData;
    }

    public void getFavourites(int userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("favourites").document(String.valueOf(userId));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Name: " + document.getData());
                        List<LocationModel> list = new ArrayList();
                        System.out.println("NOme " + (String) document.getData().get("Description"));
                        LocationModel locationObject = new LocationModel((String) document.getData().get("Name"), (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"), (long) document.getData().get("Id"),(String) document.getData().get("AverageRating"));

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

//    public void addFavouriteLocation(int userId,int locationId)
//    {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        DocumentReference docRef = db.collection("favourites").document(String.valueOf(userId)).set(locationId);
//
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Name: " + document.getData());
//                        List<LocationObject> list = new ArrayList();
//                        System.out.println("NOme " + (String) document.getData().get("Desciption"));
//                        LocationObject locationObject = new LocationObject((String) document.getData().get("Name"), (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"));
//
//                        list.add(locationObject);
//                        locationLiveData.setValue(list);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//    }

    public void removeFavouriteLocation()
    {

    }


    public void getLocationsCoordinates() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(
//        //List<LocationObject> temp = new ArrayList<>();
//        db.collection("locations").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    LocationObject locationObject = new LocationObject((String) document.getData().get("Name"), (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"));
//                    temp.add(locationObject);
//                    Log.d(TAG, document.getId() + " => " + document.getData());
//                }
//                System.out.println("Amount: " + temp.size());
//                locationLiveData.setValue(temp);
//            } else {
//                Log.d(TAG, "Error getting documents: ", task.getException());
//            }
//        });
        );}

    public void setSnap(LocationModel clickedItemIndex) {
        snapLiveData.setValue(clickedItemIndex);
    }

    public MutableLiveData<LocationModel> getSnapLiveData() {
        return snapLiveData;
    }
}
