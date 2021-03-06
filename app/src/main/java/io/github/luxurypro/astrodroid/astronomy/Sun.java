package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.util.DateUtil;
import io.github.luxurypro.astrodroid.util.MathUtil;

public class Sun extends CellestialBody {
    private final HorizontalCoordinates horizontalCoordinates;

    public Sun(double JulianDay, double latitude, double longitude) {
        double j2000 = DateUtil.toJ2000(JulianDay);
        double meanAnomaly = MathUtil.normalizeAngle(Math.toRadians(357.5291) + Math.toRadians(0.98560028) * j2000);
        double equationOfCenter = Math.toRadians(1.9148) * Math.sin(meanAnomaly) + Math.toRadians(0.020) * Math.sin(2 * meanAnomaly) + Math.toRadians(0.0003) * Math.sin(3 * meanAnomaly);
        double meanLongitude = meanAnomaly + Math.toRadians(102.9373);

        double sunMeanLongitude = meanLongitude + Math.toRadians(180);
        double sunElipticalLongitude = MathUtil.normalizeAngle(sunMeanLongitude + equationOfCenter);

        ElipticalCoordinates elipticalCoordinates = new ElipticalCoordinates(0, sunElipticalLongitude);

        this.equatorialCoordinates = EquatorialCoordinates.fromeElipticalCoordinates(elipticalCoordinates, j2000);
        horizontalCoordinates = HorizontalCoordinates.fromEquatorialCoordinates(getEquatorialCoordinates(), j2000, latitude, longitude);
    }

    public double getAzimunt() {
        return this.horizontalCoordinates.getAzimunt();
    }

    public double getAltitude() {
        return this.horizontalCoordinates.getAltitude();
    }

}
