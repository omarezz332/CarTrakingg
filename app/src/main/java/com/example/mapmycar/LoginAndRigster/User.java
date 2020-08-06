package com.example.mapmycar.LoginAndRigster;

import com.google.android.gms.maps.model.LatLng;

public class User {
   private String Displayname;
    private String Email;
    //private LatLng UserLocation;
    private String State;
    public Double latitude, longitude;

    public User (){};

    public User(String displayname, String email, String State,Double lat,Double lng){
        this.Displayname=displayname;
        this.Email=email;
        this.State=State;
        this.latitude=lat;
        this.longitude=lng;
    }


    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public void setState(String state) { State = state; }



    public String getDisplayname() {return Displayname;}
    public String getEmail() {return Email;}
    public String getState() {return State;}




}
