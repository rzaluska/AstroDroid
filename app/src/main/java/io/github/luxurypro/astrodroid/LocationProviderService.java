package io.github.luxurypro.astrodroid;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

public class LocationProviderService extends Service implements LocationListener {
    private LocationManager locationManager;
    private Location lastLocation;
    private int status = LocationProvider.OUT_OF_SERVICE;
    private boolean isGPSEnabled;

    public int getStatus() {
        return status;
    }

    public boolean isEnabled() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean manualLocation = sharedPref.getBoolean("manual_location", false);
        return isGPSEnabled || manualLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            return;
        this.lastLocation = location;
    }

    public Location getLastLocation() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean manualLocation = sharedPref.getBoolean("manual_location", false);
        if (manualLocation) {
            float latitude = Float.parseFloat(sharedPref.getString("latitude", "0"));
            float longitude = Float.parseFloat(sharedPref.getString("longitude", "0"));
            Location location = new Location(LocationManager.PASSIVE_PROVIDER);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            return location;
        }
        return this.lastLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.status = status;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocalBinder extends Binder {
        LocationProviderService getService() {
            return LocationProviderService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.connectToGPS();
        return mBinder;
    }

    public void connectToGPS() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean manualLocation = sharedPref.getBoolean("manual_location", false);
        if (manualLocation)
            return;
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        this.lastLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onCreate() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        this.locationManager.removeUpdates(this);
    }
}
