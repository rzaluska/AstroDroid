package io.github.luxurypro.astrodroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import java.util.Calendar;

import io.github.luxurypro.astrodroid.astronomy.HorizontalCoordinates;
import io.github.luxurypro.astrodroid.astronomy.Moon;
import io.github.luxurypro.astrodroid.astronomy.Star;
import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SkyActivity extends AppCompatActivity implements SensorEventListener {
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;
    private int progress;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private LowPassFilter filter;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public SkyActivity() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateOrientationAngles();
                Calendar now = DateUtil.nowUTC();
                Sun sun = new Sun(DateUtil.toJulianDay(now) + progress * 0.01, Math.toRadians(52.229676), Math.toRadians(21.012229));
                Moon moon = new Moon(DateUtil.toJulianDay(now) + progress * 0.01, Math.toRadians(52.229676), Math.toRadians(21.012229));
                double rightAscension = AngleUtil.fromHourMinSec(02, 31, 49.09);
                double declination = AngleUtil.fromDegMinSec(89, 15, 50.8);
                Star polaris = new Star(rightAscension, declination);
                HorizontalCoordinates horizontalCoordinates = polaris.getHorizontalCoordinates(DateUtil.toJulianDay(now) + progress * 0.1, Math.toRadians(52.229676), Math.toRadians(21.012229));
                SkyView view = (SkyView) findViewById(R.id.SkyView);
                view.setData(sun, moon, horizontalCoordinates, filter.getValue());
                handler.postDelayed(runnable, 100);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky);
        progress = 0;
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);
        this.seekBar.setMax(1000);
        this.seekBar.setProgress(500);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int p, boolean fromUser) {
                progress = p - 500;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        handler.post(runnable);
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

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
}
