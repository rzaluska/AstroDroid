package io.github.luxurypro.astrodroid;

import java.lang.reflect.AnnotatedElement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public static Calendar julianDayToCalendar(double JD) {
        double W = JD + 0.5;
        int X = (int) W;
        double U = W - X;
        int Y = (int) ((X + 32044.5) / 36524.25);
        int Z = X + Y - (int) (Y / 4) - 38;
        int A = Z + 1524;
        int B = (int) ((A - 122.1) / 365.25);
        int C = A - (int) (365.25 * B);
        int E = (int) (C / 30.61);
        int F = (int) (E / 14);
        int year = B - 4716 + F;
        int month = E - 1 - 12 * F;
        double D = C + U - (int) ((153 * E) / 5);

        int day = (int) D;
        double fraction = D - day;

        int seconds = (int) (24 * 60 * 60 * fraction);
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.set(year, month - 1, day, hours, minutes, seconds);
        return gregorianCalendar;
    }

    public static double toJ2000(Calendar calendar) {
        return toJulianDay(calendar) - 2451545.0;
    }

    public static double toJ2000(double julianDay) {
        return julianDay - 2451545.0;
    }

    public static double fromJ2000(double julianDay) {
        return julianDay + 2451545.0;
    }

    public static double getJ2000Now() {
        return toJ2000(nowUTC());
    }

    public static double getJulianDayNow() {
        return toJulianDay(nowUTC());
    }

    public static Calendar nowUTC() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public static double getEarthLocalSiderialTime(double J2000, double longitude) {
        return MathUtil.normalzeAngle(Math.toRadians(280.147) + Math.toRadians(360.9856235) * J2000 + longitude);
    }
}
