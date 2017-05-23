package io.github.luxurypro.astrodroid;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import static io.github.luxurypro.astrodroid.R.id.rate;

public class LocalTimeInfoActivity extends AppCompatActivity {
    private final Handler handler;
    private final Runnable runnable;
    protected LocationProviderService locationProviderService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            locationProviderService = ((LocationProviderService.LocalBinder) service).getService();
            if (!locationProviderService.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LocalTimeInfoActivity.this);
                builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
            handler.post(runnable);
        }

        public void onServiceDisconnected(ComponentName className) {
            locationProviderService = null;
        }
    };
    private boolean mIsBoundToService;

    public LocalTimeInfoActivity() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    updateFields();
                } catch (Exception e) {
                } finally {
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_tile_info_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();

    }

    @Override
    protected void onPause() {
        super.onPause();
        doUnbindService();
        handler.removeCallbacks(runnable);
    }

    void doBindService() {
        bindService(new Intent(LocalTimeInfoActivity.this, LocationProviderService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBoundToService = true;
    }


    void doUnbindService() {
        if (mIsBoundToService) {
            unbindService(mConnection);
            mIsBoundToService = false;
        }
    }

    void updateFields() {
        TextView latitude = (TextView) findViewById(R.id.latitude);
        TextView longitude = (TextView) findViewById(R.id.longitude);
        TextView localTime = (TextView) findViewById(R.id.local);
        TextView gmt = (TextView) findViewById(R.id.gmt);
        TextView julianDay = (TextView) findViewById(R.id.julianday);
        TextView j2000 = (TextView) findViewById(R.id.j2000);
        TextView lst = (TextView) findViewById(R.id.lst);
        TextView siderial = (TextView) findViewById(R.id.sidereal);
        TextView siderialgmt = (TextView) findViewById(R.id.siderealgmt);

        Location location = locationProviderService.getLastLocation();
        latitude.setText(String.format(Locale.getDefault(), "%f", location.getLatitude()));
        longitude.setText(String.format(Locale.getDefault(), "%f", location.getLongitude()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        localTime.setText(dateTimeFormatter.print(new DateTime().withZone(DateTimeZone.getDefault())));
        gmt.setText(dateTimeFormatter.print(new DateTime().withZone(DateTimeZone.UTC)));

        julianDay.setText(String.format(Locale.getDefault(), "%f", DateUtil.getJulianDayNow()));
        j2000.setText(String.format(Locale.getDefault(), "%f", DateUtil.getJ2000Now()));

        lst.setText(dateTimeFormatter.print(DateUtil.localSolarTime(location.getLongitude())));

        double dateTime = DateUtil.getEarthLocalSiderialTime(DateUtil.getJ2000Now(), Math.toRadians(location.getLongitude())) / (2 * Math.PI) - 0.5;
        double j = Math.floor(DateUtil.getJ2000Now());
        j += dateTime;
        DateTime d = new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(j)));
        siderial.setText(dateTimeFormatter.print(d));

        double gmts = MathUtil.normalzeAngle(Math.toRadians(280.147) + Math.toRadians(360.9856235) * DateUtil.getJ2000Now()) / (2 * Math.PI) - 0.5;
        ;
        j = Math.floor(DateUtil.getJ2000Now());
        j += gmts;
        d = new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(j)));
        siderialgmt.setText(dateTimeFormatter.print(d));
    }

    public void goToSettings(View v) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}

