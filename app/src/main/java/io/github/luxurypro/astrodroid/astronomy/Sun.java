package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.DateUtil;
import io.github.luxurypro.astrodroid.MathUtil;

public class Sun {
    private double azimunt;
    private double altitude;

    public Sun(double JulianDay, double latitude, double longitude, double azimuntDelta) {
        double j2000 = DateUtil.toJ2000(JulianDay);
        double meanAnomaly = MathUtil.normalzeAngle(Math.toRadians(357.5291) + Math.toRadians(0.98560028) * j2000);
        double equationOfCenter = Math.toRadians(1.9148) * Math.sin(meanAnomaly) + Math.toRadians(0.020) * Math.sin(2 * meanAnomaly) + Math.toRadians(0.0003) * Math.sin(3 * meanAnomaly);
        double trueAnomaly = meanAnomaly + equationOfCenter;
        double meanLongitude = meanAnomaly + Math.toRadians(102.9373);
        double elipticLongitude = meanLongitude + equationOfCenter;
        double sunMeanLongitude = meanLongitude + Math.toRadians(180);
        double sunElipticalLongitude = MathUtil.normalzeAngle(sunMeanLongitude + equationOfCenter);
        double rightAscension = Math.atan2(Math.sin(sunElipticalLongitude) * Math.cos(Math.toRadians(23.4393)), Math.cos(sunElipticalLongitude));
        double declination = Math.asin(Math.sin(sunElipticalLongitude * Math.sin(Math.toRadians(23.4393))));

        double siderialTime = MathUtil.normalzeAngle(Math.toRadians(280.1470) + Math.toRadians(360.9856235) * j2000 + longitude);

        double hourAngle = siderialTime - rightAscension;
        this.azimunt = MathUtil.normalzeAngle(Math.atan2(Math.sin(hourAngle), Math.cos(hourAngle) * Math.sin(latitude) - Math.tan(declination) * Math.cos(latitude))) - azimuntDelta;
        this.altitude = Math.asin(Math.sin(latitude) * Math.sin(declination) + Math.cos(latitude) * Math.cos(declination) * Math.cos(hourAngle));
    }

    public double getAzimunt() {
        return this.azimunt;
    }

    public double getAltitude() {
        return this.altitude;
    }

}
