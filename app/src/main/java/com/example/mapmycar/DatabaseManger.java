package com.example.mapmycar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManger {

    private DatabaseReference mDatabase_lng;
    private DatabaseReference mDatabase_lat;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //DatabaseReference myRef = database.getReference("message");
     public DatabaseManger() {
         mDatabase_lng = database.getReference().child("carLongitude");
         mDatabase_lat = database.getReference().child("carLatitude");
    }

    public void setCarlocation(String lng, String lat) {
         mDatabase_lng.setValue(5.354);
         mDatabase_lat.setValue(3.456);
    }
}
