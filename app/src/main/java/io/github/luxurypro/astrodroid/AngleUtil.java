package io.github.luxurypro.astrodroid;

public class AngleUtil {
    public static double fromHourMinSec(int hour, int minute, double sec) {
        return 2 * Math.PI * (hour / 24.0 + minute / (60.0 * 24.0) + sec / (60.0 * 60.0 * 24.0));
    }

    public static double fromDegMinSec(int deg, int minute, double sec) {
        return Math.signum(deg) * 2 * Math.PI * (Math.abs(deg) / 360.0 + minute / (3600.0 * 60.0) + sec / (360.0 * 60.0 * 60.0));
    }
}

