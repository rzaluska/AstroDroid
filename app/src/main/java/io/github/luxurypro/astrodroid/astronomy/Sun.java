package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.DateUtil;
import io.github.luxurypro.astrodroid.MathUtil;

public class Sun {
    private double azimunt;
    private double altitude;
    public Sun(double JulianDay, double latitude, double longitude) {
        this.azimunt = 0;
        this.altitude = 0;
        double n = JulianDay - 2451545.0;
        double tilt = Math.toRadians(23.439) - Math.toRadians(0.0000004) * n;
        double meanLongitude = Math.toRadians(280.46) + Math.toRadians(0.9856474) * n;
        meanLongitude = MathUtil.normalzeAngle(meanLongitude);
        double meanAnomaly = Math.toRadians(357.528) + Math.toRadians(0.9856003) * n;
        meanAnomaly = MathUtil.normalzeAngle(meanAnomaly);
        double elipticLongitude = meanLongitude + Math.toRadians(1.915) * Math.sin(meanAnomaly) + Math.toRadians(0.020) * Math.sin(2 * meanAnomaly);
        double elipticLatitude = 0;
        double distanceToSun = 1.00014 - 0.01671 * Math.cos(meanAnomaly) - 0.00014 * Math.cos(2 * meanAnomaly);

        // Equatorial coordinates
        double rightAscension = Math.atan2(Math.cos(tilt) * Math.sin(elipticLongitude), Math.cos(elipticLongitude));
        double declination = Math.asin(Math.sin(tilt) * Math.sin(elipticLongitude));

        double siderialTime = DateUtil.getEarthSiderialTime(n, longitude);

        double hourAngle = siderialTime - rightAscension;
        this.azimunt = Math.PI + MathUtil.normalzeAngle(Math.atan2(Math.sin(hourAngle), Math.cos(hourAngle) * Math.sin(latitude) - Math.tan(declination) * Math.cos(latitude)));
        this.altitude = Math.PI/2.0 - Math.acos(Math.sin(latitude) * Math.sin(declination) + Math.cos(latitude) * Math.cos(declination) * Math.cos(hourAngle));
    }

    public double getAzimunt() {
        return this.azimunt;
    }

    public double getAltitude() {
        return this.altitude;
    }

}
