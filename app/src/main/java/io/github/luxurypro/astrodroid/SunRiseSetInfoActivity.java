package io.github.luxurypro.astrodroid;


import android.location.Location;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import io.github.luxurypro.astrodroid.astronomy.EquatorialCoordinates;
import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SunRiseSetInfoActivity extends RiseSetInfoActivity {
    @Override
    public void doUpdateTitle(TextView title) {
        title.setText("Sun");
    }

    @Override
    public void doUpdateFields(TextView rise, TextView transit, TextView set) {
        Location location = locationProviderService.getLastLocation();
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        double julianDay = DateUtil.toJulianDay(date);
        Sun sun = new Sun(julianDay, Math.toRadians(location.getLatitude()), Math.toRadians(location.getLongitude()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String t = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        sun.getTransit(Math.toRadians(location.getLongitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        transit.setText(t);

        String r = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        sun.getRise(Math.toRadians(location.getLongitude()), Math.toRadians(location.getLatitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        rise.setText(r);

        String s = dateTimeFormatter.print(
                new DateTime(DateUtil.julianDayToCalendar(DateUtil.fromJ2000(
                        sun.getSet(Math.toRadians(location.getLongitude()), Math.toRadians(location.getLatitude()))
                ))).withZone(DateTimeZone.getDefault())
        );
        set.setText(s);
    }
}
