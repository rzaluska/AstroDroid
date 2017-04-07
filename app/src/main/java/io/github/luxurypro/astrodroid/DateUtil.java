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

        double a = Math.floor((14 - month) / 12);
        double y = year + 4800 - a;
        double m = month + 12 * a - 3;

        double jdn = day + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - Math.floor(y / 100) + Math.floor(y / 400) - 32045;
        double fraction = (float) hour / 24 + (float) minute / (60 * 24) + (float) second / (3600 * 24) - 0.5;
        return jdn + fraction;
    }

    public static double toJ2000(Calendar calendar) {
        return toJulianDay(calendar) - 2451545.0;
    }

    public static Calendar nowUTC() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public static double getEarthSiderialTime(double J2000, double longitude) {
        return MathUtil.normalzeAngle(Math.toRadians(280.147) + Math.toRadians(360.9856235) * J2000 + longitude);
    }

}
