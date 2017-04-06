package io.github.luxurypro.astrodroid;

import android.icu.util.GregorianCalendar;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.util.Calendar;

import io.github.luxurypro.astrodroid.R;
import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SkyActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;
    private SeekBar seekBar;
    private int progress;

    public SkyActivity() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar now = DateUtil.nowUTC();
                Sun sun = new Sun(DateUtil.toJulianDay(now) + progress * 0.1, Math.toRadians(52.229676), Math.toRadians(21.012229));
                double azimunt = sun.getAzimunt();
                double altitude = sun.getAltitude();
                SkyView view = (SkyView) findViewById(R.id.SkyView);
                view.setData(azimunt, altitude);
                handler.postDelayed(runnable, 10);
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
        handler.post(runnable);
    }
}
