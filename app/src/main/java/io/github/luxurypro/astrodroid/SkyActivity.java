package io.github.luxurypro.astrodroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.github.luxurypro.astrodroid.astronomy.Moon;
import io.github.luxurypro.astrodroid.astronomy.SkyMap;
import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SkyActivity extends AppCompatActivity implements SensorEventListener {
    private final TimeManager timeManager;
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;
    private int progress;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private LowPassFilter filter;
    private SkyMap skyMap;
    int rate = 1;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private LocationProviderService locationProviderService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            locationProviderService = ((LocationProviderService.LocalBinder) service).getService();
            if (!locationProviderService.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SkyActivity.this);
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
        }

        public void onServiceDisconnected(ComponentName className) {
            locationProviderService = null;
        }
    };
    private boolean mIsBoundToService;
    private int previousRate = 1;
    private boolean stop;

    public SkyActivity() throws IOException, JSONException {
        handler = new Handler();
        timeManager = new TimeManager();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    timeManager.tick(rate);
                    updateOrientationAngles();
                    updateDateTimeText();
                    double julianDate = timeManager.getJulianDate();
                    Location location = locationProviderService.getLastLocation();
                    double latitude = Math.toRadians(location.getLatitude());
                    double longitude = Math.toRadians(location.getLongitude());
                    Sun sun = new Sun(julianDate, latitude, longitude);
                    Moon moon = new Moon(julianDate, latitude, longitude);
                    SkyView view = (SkyView) findViewById(R.id.SkyView);
                    skyMap.updatePosition(julianDate, latitude, longitude);
                    view.setData(sun, moon, skyMap, filter.getValue());
                } catch (Exception e) {
                } finally {
                    if (!stop)
                        handler.postDelayed(runnable, 16);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky);
        AssetManager assetManager = this.getAssets();
        try {
            this.skyMap = SkyMap.readFromStream(assetManager.open("AstroCatalogue/starts.json"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        handler.post(runnable);
        updateDateTimeText();
        updateRateText();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);
        // "mRotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        if (this.filter == null) {
            this.filter = new LowPassFilter(mOrientationAngles[0]);
        }
        filter.update(mOrientationAngles[0]);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
        doUnbindService();
    }

    void doBindService() {
        bindService(new Intent(SkyActivity.this, LocationProviderService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBoundToService = true;
    }


    void doUnbindService() {
        if (mIsBoundToService) {
            unbindService(mConnection);
            mIsBoundToService = false;
        }
    }

    public void onChangeDateTime(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.date_time_picker_layout);
        dialog.setCancelable(true);

        final DateTime currentTime = timeManager.getCurrentDateTimeUTC().withZone(DateTimeZone.getDefault());
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(this));
        timePicker.setHour(currentTime.getHourOfDay());
        timePicker.setMinute(currentTime.getMinuteOfHour());
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        datePicker.init(currentTime.getYear(), currentTime.getMonthOfYear() - 1, currentTime.getDayOfMonth(), null);

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Calendar calendar = new GregorianCalendar();
                calendar.set(year, month, day, hour, minute, 0);
                timeManager.setCurrentDateTime(calendar);
                timeManager.resume();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        timeManager.stop();
    }

    public void setCurrentDate(double currentDate) {
        handler.removeCallbacksAndMessages(null);
        this.stop = true;
        //this.currentDate = currentDate;
        stop = false;
        handler.post(this.runnable);
    }

    public void increaseRate(View v) {
        if (rate == 0 || rate == -1) {
            rate = 1;
            return;
        }
        if (rate > 0)
            rate *= 2;
        else
            rate /= 2;
        updateRateText();
    }

    public void decreaseRate(View v) {
        if (rate == 0 || rate == 1) {
            rate = -1;
            return;
        }

        if (rate > 0)
            rate /= 2;
        else
            rate *= 2;
        updateRateText();
    }

    public void playPause(View v) {
        Button playPauseButton = (Button) findViewById(R.id.playPause);
        if (rate == 0) {
            //play
            this.rate = this.previousRate;
            playPauseButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        } else {
            //pause
            this.previousRate = rate;
            this.rate = 0;
            playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
        updateRateText();
    }

    public void resetDate(View v) {
        timeManager.resetToCurrentTimeDate();
    }

    public void updateRateText() {
        TextView rateView = (TextView) findViewById(R.id.rate);
        rateView.setText("x" + String.valueOf(rate));
    }

    public void updateDateTimeText() {
        DateTime currentDateTime = timeManager.getCurrentDateTimeUTC();
        TextView currentdateTime = (TextView) findViewById(R.id.currentDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = dateTimeFormatter.print(currentDateTime.withZone(DateTimeZone.getDefault()));
        currentdateTime.setText(dateString);
    }

    public void resetRate(View view) {
        this.rate = 1;
        this.updateRateText();
    }
}
