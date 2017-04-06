package io.github.luxurypro.astrodroid;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static double toJulianDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        double extra = (100.0 * year) + month - 190002.5;
        return (367.0 * year) -
                (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0)) +
                Math.floor((275.0 * month) / 9.0) +
                day + ((hour + ((minute + (second / 60.0)) / 60.0)) / 24.0) +
                1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
    }

    public  static double toJ2000(Calendar calendar) {
        return toJulianDay(calendar) - 2451545.0;
    }

    public static Calendar nowUTC() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }
    public static double getEarthSiderialTime(double J2000, double longitude)
    {
        return  MathUtil.normalzeAngle(Math.toRadians(280.147) + Math.toRadians(360.9856235) * J2000 + longitude);
    }

}
