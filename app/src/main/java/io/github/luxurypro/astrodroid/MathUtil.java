package io.github.luxurypro.astrodroid;


public class MathUtil {
    public static double normalzeAngle(double radians) {
        double result = radians % Math.toRadians(360);
        if (result < 0)
            result += Math.toRadians(360);
        return result;
    }
}
