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
  private final MutableLiveData<Integer> resultData;

  private FavouriteRepository(Application application) {
    locationLiveData = new MutableLiveData<>();
    resultData = new MutableLiveData<>();
    resultData.setValue(0);
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
          resultData.postValue(1);
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
          ArrayList<Long> favs = new ArrayList<>();
          favs = (ArrayList<Long>) document.getData().get("favs");

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

    /*
    public void addFavouriteLocation(Context context, int userId, int locationId)
    {
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


    }*/


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
    );
  }



  public MutableLiveData<Integer> getResultData() {
    return resultData;
  }

  public void clearResultData() {
   resultData.setValue(0);
  }

  public void deleteFavourite(LocationModel clickedItemIndex) {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("favourites").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    docRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
          ArrayList<Long> favs = new ArrayList<>();
          favs = (ArrayList<Long>) document.getData().get("favs");

          System.out.println(favs.get(0));
          System.out.println(clickedItemIndex.getId());
          if (favs.contains((new Long(clickedItemIndex.getId()) )))
          {
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            favs.remove(clickedItemIndex.getId());
          }
          docRef.update("favs",favs);
          System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        } else {
          Log.d(TAG, "No such document");
        }
      } else {
        Log.d(TAG, "get failed with ", task.getException());
      }
    });






  }
}
