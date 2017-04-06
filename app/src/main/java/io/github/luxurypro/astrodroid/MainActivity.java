package io.github.luxurypro.astrodroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "io.github.luxurypro.astrodroid.MESSAGE";
    private Handler someHandler;
    private TextView jDateField;
    private LocationManager locationManager;
    private TextView latitude;
    private TextView longitude;

    public MainActivity() {
        this.someHandler = new Handler();
    }

    private TextView dateField;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.v(TAG, "Running loop");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            double julianDay = DateUtil.toJulianDay(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            dateField.setText(currentDateTimeString);
            jDateField.setText(String.format(new Locale("PL"), "%f", julianDay));
            someHandler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        dateField = (TextView) this.findViewById(R.id.data);
        jDateField = (TextView) this.findViewById(R.id.dataJ);
        latitude = (TextView) this.findViewById(R.id.gpsLat);
        longitude = (TextView) this.findViewById(R.id.gpsLon);
        someHandler.post(runnable);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        someHandler.removeCallbacks(this.runnable);
        this.locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        someHandler.post(this.runnable);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        someHandler.removeCallbacks(this.runnable);
        someHandler = null;
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.v(TAG, "location changed");
        if (location == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double lon = location.getLongitude();
                double lat = location.getLatitude();
                longitude.setText(String.format(new Locale("PL"), "%f", lon));
                latitude.setText(String.format(new Locale("PL"), "%f", lat));

            }
        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(this.getApplicationContext(), status + " - New status", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void runSkyView(View view) {
        startActivity(new Intent(this, SkyActivity.class));
    }
}
