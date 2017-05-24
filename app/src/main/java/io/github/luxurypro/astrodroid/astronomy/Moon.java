package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.util.DateUtil;
import io.github.luxurypro.astrodroid.util.MathUtil;

public class Moon extends CellestialBody {
    private HorizontalCoordinates horizontalCoordinates;

    public Moon(double JulianDay, double latitude, double longitude) {
        double j2000 = DateUtil.toJ2000(JulianDay);

        ElipticalCoordinates elipticalCoordinates = Moon.getElipticalCoordinates(j2000);

        this.equatorialCoordinates = EquatorialCoordinates.fromeElipticalCoordinates(elipticalCoordinates, j2000);

        this.horizontalCoordinates = HorizontalCoordinates.fromEquatorialCoordinates(equatorialCoordinates, j2000, latitude, longitude);
    }

    private static ElipticalCoordinates getElipticalCoordinates(double j2000) {
        double L = MathUtil.normalizeAngle(Math.toRadians(218.316) + Math.toRadians(13.176396) * j2000);
        double M = getMeanAnomaly(j2000);
        double F = MathUtil.normalizeAngle(Math.toRadians(93.272) + Math.toRadians(13.229350) * j2000);

        double elipticalLongitude = L + Math.toRadians(6.289) * Math.sin(M); //lambda
        double elipticalLatitude = Math.toRadians(5.128) * Math.sin(F); // beta
        return new ElipticalCoordinates(elipticalLatitude, elipticalLongitude);
    }

    private static double getMeanAnomaly(double j2000) {
        return MathUtil.normalizeAngle(Math.toRadians(134.963) + Math.toRadians(13.064993) * j2000);
    }

    /**
     * @param j2000
     * @return distance to Moon in Km
     */
    public double getDistance(double j2000) {
        double M = getMeanAnomaly(j2000);
        return 385001 - 20905 * Math.cos(M);
    }

    public double getAzimunt() {
        return this.horizontalCoordinates.getAzimunt();
    }

    public double getAltitude() {
        return this.horizontalCoordinates.getAltitude();
    }
}
