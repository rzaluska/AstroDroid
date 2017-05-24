package io.github.luxurypro.astrodroid.astronomy;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import io.github.luxurypro.astrodroid.util.MathUtil;

import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

public class Jupiter implements AstroObject {
    private HorizontalCoordinates horizontalCoordinates;

    @Override
    public void updatePosition(double j2000, double latitude, double longitude) {
        final double n = Math.toRadians(0.083056);
        final double ae = 5.19037;
        final double Pi = Math.toRadians(14.331);
        final double e = 0.04849;
        final double i = Math.toRadians(1.303);
        final double omega = Math.toRadians(273.867);
        final double OM = Math.toRadians(100.464);
        final double Mzero = Math.toRadians(20.020);

        final double M = MathUtil.normalizeAngle(Mzero + j2000 * n);
        final double nu = calculateTrueAnomaly(M, e);

        final double r = ae / (1 + e * cos(nu));

        double x = r * (cos(OM) * cos(omega + nu) - sin(OM) * cos(i) * sin(omega + nu));
        double y = r * (sin(OM) * cos(omega + nu) + cos(OM) * cos(i) * sin(omega + nu));
        double z = r * sin(i) * sin(omega + nu);

        final double[] earthPos = calcEarthPosition(j2000);


        x = earthPos[0] - x;
        y = earthPos[1] - y;
        z = earthPos[2] - z;

        final double DELTA = sqrt(x * x + y * y + z * z);

        final double lambda = atan2(y, x);
        final double beta = asin(z / DELTA);

        ElipticalCoordinates elipticalCoordinates = new ElipticalCoordinates(beta, lambda);
        EquatorialCoordinates equatorialCoordinates = EquatorialCoordinates.fromeElipticalCoordinates(elipticalCoordinates, j2000);
        this.horizontalCoordinates = HorizontalCoordinates.fromEquatorialCoordinates(equatorialCoordinates, j2000, latitude, longitude);
    }

    private static double calculateTrueAnomaly(double M, double e) {
        double eccentricAnomaly = M;
        int i = 100;
        while (i > 0) {
            eccentricAnomaly = eccentricAnomaly + (M + e * sin(eccentricAnomaly) - eccentricAnomaly) / (1 - e * cos(eccentricAnomaly));
            //eccentricAnomaly = eccentricAnomaly + e * sin(eccentricAnomaly);
            i--;
        }
        return MathUtil.normalizeAngle(atan(sqrt((1 + e) / (1 - e)) * tan(0.5 * eccentricAnomaly)) * 2);
    }


    public double[] calcEarthPosition(double j2000) {
        double[] pos = new double[3];

        final double n = Math.toRadians(0.985608);
        final double ae = 0.99972;
        final double Pi = Math.toRadians(102.937);
        final double e = 0.01671;
        final double i = Math.toRadians(0);
        final double omega = Math.toRadians(288.064);
        final double OM = Math.toRadians(174.873);
        final double Mzero = Math.toRadians(357.529);

        final double M = MathUtil.normalizeAngle(Mzero + j2000 * n);
        final double nu = calculateTrueAnomaly(M, e);

        final double r = ae / (1 + e * cos(nu));

        pos[0] = r * (cos(OM) * cos(omega + nu) - sin(OM) * cos(i) * sin(omega + nu));
        pos[1] = r * (sin(OM) * cos(omega + nu) + cos(OM) * cos(i) * sin(omega + nu));
        pos[2] = r * sin(i) * sin(omega + nu);
        return pos;
    }

    @Override
    public double getAzimunt() {
        return horizontalCoordinates.getAzimunt();
    }

    @Override
    public double getAltitude() {
        return horizontalCoordinates.getAltitude();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, double radius, Point middle) {
        if (this.getAltitude() < 0)
            return;
        paint.setColor(Color.parseColor("#ffa700"));
        double r = radius * Math.tan((Math.PI / 2 - this.getAltitude()) / 2);
        //double r = radius * ((Math.PI / 2) - this.getAltitude()) / (Math.PI / 2);
        double alpha = this.getAzimunt() + Math.PI / 2;
        int sx = (int) (r * Math.cos(alpha));
        int sy = (int) (r * Math.sin(alpha));
        canvas.drawCircle(middle.x + sx, middle.y + sy, (float) (radius * 0.05), paint);

    }
}
