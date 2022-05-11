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
import com.example.handiwalk.Models.ReviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void addFavouriteLocation(LocationModel locationModel, int locationId)
    {

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null)
        {
            Toast.makeText(context,"Log in first",Toast.LENGTH_SHORT).show();
        }
        else {
            //Setup data to add in firebase db of current user favorite location
            HashMap<Integer,Object> hashMap = new HashMap<>();
            hashMap.put(userId,locationId);

            //Save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
            ref.child(firebaseAuth.getUid()).child("favourites").child(String.valueOf(locationId))
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context,"Added to favourite",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Task<Void> docRef = db.collection("favourites").document(String.valueOf(userId)).set(locationId);
        //  DocumentReference docRef = db.collection("favourites").document(String.valueOf(userId));
/*
        Task<Void> docRef = db.collection("favourites").document(String.valueOf(userId))
                .set(locationId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"DocumentSnapshot written!");
                        List<LocationModel> list = new ArrayList();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error writing document");
                    }
                });
            /*
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

             */
    }





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
