package com.example.mapmycar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManger {
    private DatabaseReference mDatabase_lng;
    private DatabaseReference mDatabase_lat;
    FirebaseDatabase database_lng = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("message");
     public DatabaseManger(){

    }

    public void setCarlocation(String lng,String lat) {
        mDatabase_lng = FirebaseDatabase.getInstance().getReference().child(lng);
        mDatabase_lat = FirebaseDatabase.getInstance().getReference().child(lat);

    }
}
