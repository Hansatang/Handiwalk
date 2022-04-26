package com.example.handiwalk;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LocationLiveData extends LiveData<LocationObject> {
    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            LocationObject locationObject = snapshot.getValue(LocationObject.class);
            setValue(locationObject);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    DatabaseReference databaseReference;

    public LocationLiveData(DatabaseReference ref) {
        databaseReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(listener);
    }
}
