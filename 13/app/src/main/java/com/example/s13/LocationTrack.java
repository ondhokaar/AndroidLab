package com.example.s13;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class LocationTrack {


    private static final long LOCATION_REFRESH_TIME = 1;
    private static final float LOCATION_REFRESH_DISTANCE = 10;
    String currentLocation = "unknown";
    Context activityContext;

    public LocationTrack(Context activityContext) {
        this.activityContext = activityContext;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = location.toString();
        }

    };

    void updateLocation() {
        LocationManager mLocationManager = (LocationManager) activityContext.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            Toast.makeText(activityContext, "permission denied : location - " + currentLocation, Toast.LENGTH_SHORT).show();


        } mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
        Log.d("location: ", "permission denied  : LocatioinTrack -> updateLocation()");
    }


    public String getCurrentLocation() {
        return currentLocation;
    }
}
