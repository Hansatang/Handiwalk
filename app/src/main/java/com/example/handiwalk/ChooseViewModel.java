package com.example.handiwalk;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

public class ChooseViewModel {
    private final LocationRepository locationRepository;
    publicChooseViewModel(Application app){
        super(app);

        locationRepository = MessageRepository.getInstance();
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        locationRepository.init(userId);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void saveMessage(String message) {
        locationRepository.saveMessage(message);
    }

    public LiveData<Message> getMessage() {
        return locationRepository.getMessage();
    }

    public void signOut() {
        userRepository.signOut();
    }
}
