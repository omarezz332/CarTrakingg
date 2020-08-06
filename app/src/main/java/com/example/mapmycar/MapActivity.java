package com.example.mapmycar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mapmycar.LoginAndRigster.Register;
import com.example.mapmycar.LoginAndRigster.User;
import com.example.mapmycar.directionhelpers.FetchURL;
import com.example.mapmycar.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {//}, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private View mapView;
    private List<User> users;
    private MarkerOptions place1, place2;
    private DatabaseManger manger = new DatabaseManger();
    private Polyline currentPolyline;
    private final float DEFAULT_ZOOM = 17;
    private LatLng myplace, destnation;
    private boolean flag = true;
    Marker dist,mycarpin;
    private List<LatLng> carsLatLngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);


        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                mLastKnownLocation = locationResult.getLastLocation();
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                manger.setCarlocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                myplace = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());///////

        getData();
    }
    private void getData() {

       int height = 130;
        int width = 130;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.car);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        // DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LatLng userlocation;
                carsLatLngs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    assert user != null;
                   // LatLng location=new LatLng(user.latitude, user.longitude);
                     LatLng newLocation = new LatLng(dataSnapshot.child("latitude").getValue(double.class), dataSnapshot.child("longitude").getValue(double.class));
                    userlocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.clear();
                   //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                    //Toast.makeText(MapActivity.this, String.valueOf(location), Toast.LENGTH_SHORT).show();
                    if (user.getState().equals("Car")) {
                        //userlocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        carsLatLngs.add(newLocation);

                    }
                    showMyCar(carsLatLngs);
                }

                //Toast.makeText(MapActivity.this, carsLatLngs.size(), Toast.LENGTH_SHORT).show();
            }

            private void showMyCar(List<LatLng> carsLatLngs) {
                for (LatLng latLng : carsLatLngs) {
                    Toast.makeText(MapActivity.this, String.valueOf(latLng), Toast.LENGTH_SHORT).show();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("car")).
                            setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                //mycarpin.setVisible(true);
                //mycarpin.showInfoWindow();
                 }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
       mMap = googleMap;
       mMap.setOnMarkerClickListener( this);
        mMap.setMapType(R.raw.google_style);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
       /* mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                destnation=latLng;
                dist = mMap.addMarker(new MarkerOptions().position(latLng).visible(true).alpha(.8f).title("Destnation"));
                dist.showInfoWindow();
                new FetchURL(MapActivity.this).execute(getUrl(myplace, destnation, "driving"), "driving");

            }
        });*/

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
                new FetchURL(MapActivity.this).execute(getUrl(myplace, markerOptions.getPosition(), "driving"), "driving");
            }
        });




        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();

            }
        });



    }



    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                manger.setCarlocation(mLastKnownLocation.getLongitude(),mLastKnownLocation.getLatitude());
                            }

                        } else {
                            Toast.makeText(MapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
   private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
////            Log.d(TAG, "Location update resumed .....................");
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
////        Log.d(TAG, "onStop fired ..............");
//        mGoogleApiClient.disconnect();
////        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
////        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
//        startLocationUpdates();
//    }
//
//    protected void startLocationUpdates() {
//        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, locationRequest, this);
////        Log.d(TAG, "Location update started ..............: ");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
////        Log.d(TAG, "Connection failed: " + connectionResult.toString());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
////        Log.d(TAG, "Firing onLocationChanged..............................................");
////        mCurrentLocation = location;
////        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
////        updateUI();
//        manger.setCarlocation(mLastKnownLocation.getLongitude(),mLastKnownLocation.getLatitude());
//        Toast.makeText(getBaseContext(), "new location " + mLastKnownLocation.getLongitude() + " " + mLastKnownLocation.getLatitude(), Toast.LENGTH_LONG);
//    }



}
