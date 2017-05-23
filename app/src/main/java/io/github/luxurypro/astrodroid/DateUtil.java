package io.github.luxurypro.astrodroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.lang.reflect.AnnotatedElement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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

    /**
     * Calculate local solar time for now in given longitude
     * @param longitude in degrees
     * @return DateTime for local solar time
     */
    public static DateTime localSolarTime(double longitude) {
        DateTime localTime = new DateTime().withZone(DateTimeZone.getDefault());
        DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
        Period delta = new Period(utc.toLocalDateTime(), localTime.toLocalDateTime());
        Duration lstm = delta.toStandardDuration();
        int dayOfYear = localTime.getDayOfYear();
        double B = (360.0 / 365.0) * (dayOfYear - 81);
        double EoT = 9.87 * sin(2 * B) - 7.53 * cos(B) - 1.5 * sin(B);
        Duration timeDiff = new Duration((long)(1000 * 60 * 4 * longitude));
        Duration timeCorrection = timeDiff.minus(lstm.getMillis()).withDurationAdded((long) (EoT * 1000 * 60), 1);
        return new DateTime().withDurationAdded(timeCorrection.getMillis(), 1);
    }
}
