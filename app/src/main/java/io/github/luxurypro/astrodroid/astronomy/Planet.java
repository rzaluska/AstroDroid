package io.github.luxurypro.astrodroid.astronomy;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

public class Planet implements AstroObject {
    private double semimajorAxis; //a
    private double eccentircity; //e
    private double argumentOfPerihelion; //omega
    private double eclipticLongitude; //Ometa
    private double inclination; //i
    private double meanAnomaly; //M

    private double calculateTrueAnomay(double M) {
        double eccentricAnomaly = M;
        int i = 100;
        while (i > 0) {
            eccentricAnomaly = eccentricAnomaly + (M + eccentircity * sin(eccentricAnomaly) - eccentricAnomaly) / (1 - eccentircity * cos(eccentricAnomaly));
            i--;
        }
        return atan(sqrt((1 + eccentircity) / (1 - eccentircity)) * tan(0.5 * eccentricAnomaly));
    }

    @Override
    public void updatePosition(double j2000, double latitude, double longitude) {
        double n = Math.toRadians(0.9856076686) / Math.pow(this.semimajorAxis, 3.0 / 2.0);
        double M = this.meanAnomaly + n * j2000;
        double trueAnomaly = calculateTrueAnomay(M);
        double distanceToSun = (semimajorAxis * (1 - eccentircity * eccentircity)) / (1 + eccentircity * cos(trueAnomaly));
    }

    @Override
    public double getAzimunt() {
        return 0;
    }

    @Override
    public double getAltitude() {
        return 0;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, double radius, Point middle) {

    }
}
