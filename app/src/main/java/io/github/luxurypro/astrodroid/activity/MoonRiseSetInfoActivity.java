package io.github.luxurypro.astrodroid.activity;


import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import io.github.luxurypro.astrodroid.util.DateUtil;
import io.github.luxurypro.astrodroid.R;
import io.github.luxurypro.astrodroid.astronomy.Moon;

public class MoonRiseSetInfoActivity extends RiseSetInfoActivity {
    @Override
    protected void doSetBackground() {
        ImageView view = (ImageView) findViewById(R.id.planetImage);
        view.setImageResource(R.drawable.moon);
    }

    @Override
    public void doUpdateTitle(TextView title) {
        title.setText("Moon");
    }

    @Override
    public void doUpdateFields(TextView rise, TextView transit, TextView set) {
        Location location = locationProviderService.getLastLocation();
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        double julianDay = DateUtil.toJulianDay(date);
        Moon moon = new Moon(julianDay, Math.toRadians(location.getLatitude()), Math.toRadians(location.getLongitude()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String t = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        moon.getTransit(Math.toRadians(location.getLongitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        transit.setText(t);

        String r = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        moon.getRise(Math.toRadians(location.getLongitude()), Math.toRadians(location.getLatitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        rise.setText(r);

        String s = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        moon.getSet(Math.toRadians(location.getLongitude()), Math.toRadians(location.getLatitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        set.setText(s);
    }
}
