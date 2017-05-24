package io.github.luxurypro.astrodroid.util;


public class MathUtil {
    public static double normalizeAngle(double radians) {
        double result = radians % Math.toRadians(360);
        if (result < 0)
            result += Math.toRadians(360);
        return result;
    }

    public static double radiansFromHourMinSec(int hour, int minute, double sec) {
        return 2 * Math.PI * (hour / 24.0 + minute / (60.0 * 24.0) + sec / (60.0 * 60.0 * 24.0));
    }

    public static double radiansFromDegMinSec(int deg, int minute, double sec) {
        return Math.signum(deg) * 2 * Math.PI * (Math.abs(deg) / 360.0 + minute / (3600.0 * 60.0) + sec / (360.0 * 60.0 * 60.0));
    }
}
