package com.example.mapmycar.model;

public class Car {
    String CarID;
    double lat,lng;
    public Car(String CarID,double lat,double lng){
        this.CarID=CarID;
        this.lat=lat;
        this.lng=lng;
    }
    public double getLat(){return lat;}
    public double getLng(){return lng;}
    public String getCarID(){return CarID;}

}
