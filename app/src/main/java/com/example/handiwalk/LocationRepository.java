package com.example.handiwalk;

public class LocationRepository {
    {
        private static LocationRepository instance;
        private DatabaseReference myRef;
        private MessageLiveData message;

    private LocationRepository(){}

        public static synchronized LocationRepository getInstance() {
        if(instance == null)
            instance = new LocationRepository();
        return instance;
    }

        public void init(String userId) {
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        message = new MessageLiveData(myRef);
    }

        public void saveMessage(String message) {
        myRef.setValue(new Message(message));
    }

        public MessageLiveData getMessage() {
        return message;
    }
}
