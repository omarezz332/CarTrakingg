package com.example.mapmycar;

import android.widget.Toast;

import com.example.mapmycar.LoginAndRigster.DashboardActivity;
import com.example.mapmycar.LoginAndRigster.Register;
import com.example.mapmycar.LoginAndRigster.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class DatabaseManger {

    public static String Userid;
    private DatabaseReference mDatabase_lng;
    private DatabaseReference mDatabase_lat;
    private DatabaseReference mDatabase_LatLng;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private User user;
    //DatabaseReference myRef = database.getReference("message");

     public DatabaseManger() {
         mDatabase_lat  = database.getReference().child("Users").child(Userid).child("latitude");
         mDatabase_lng = database.getReference().child("Users").child(Userid).child("longitude");
         mDatabase_LatLng = database.getReference().child("Users").child(Userid);
    }


    public void setCarlocation( double lat,double lng) {
        LatLng latLng=new LatLng(lng,lat);
        mDatabase_lat.setValue(lat);
        mDatabase_lng.setValue(lng);

       // user=new User(lng,lat);
       // mDatabase_LatLng.setValue(user);
    }
}
