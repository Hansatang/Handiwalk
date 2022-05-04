package com.example.handiwalk;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {

    private static LocationRepository instance;
    private final MutableLiveData<List<LocationObject>> locationLiveData;
    private final MutableLiveData<LocationObject> snapLiveData;

    private LocationRepository(Application application) {
        locationLiveData = new MutableLiveData<>();
        snapLiveData = new MutableLiveData<>();
        //getLocations();
        getLocationsCoordinates();
    }

    public static synchronized LocationRepository getInstance(Application application) {
        if (instance == null)
            instance = new LocationRepository(application);
        return instance;
    }


    public LiveData<List<LocationObject>> getLocationLiveData() {
        return locationLiveData;
    }

    public void getLocations() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("locations").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Name: " + document.getData());
                        List<LocationObject> list = new ArrayList();
                        System.out.println("NOme " + (String) document.getData().get("Desciption"));
                        LocationObject locationObject = new LocationObject((String) document.getData().get("Name"), (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"));

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
        List<LocationObject> temp = new ArrayList<>();
        db.collection("locations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    LocationObject locationObject = new LocationObject((String) document.getData().get("Name"), (GeoPoint) document.getData().get("Coordinates"), (String) document.getData().get("Description"));
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

    public void setSnap(LocationObject clickedItemIndex) {
        snapLiveData.setValue(clickedItemIndex);
    }

    public MutableLiveData<LocationObject> getSnapLiveData() {
        return snapLiveData;
    }
}