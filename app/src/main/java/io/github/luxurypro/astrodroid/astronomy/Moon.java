package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.DateUtil;
import io.github.luxurypro.astrodroid.MathUtil;

public class Moon {
    private double azimunt;
    private double altitude;

    public Moon(double JulianDay, double latitude, double longitude, double azimuntDelta) {
        double j2000 = DateUtil.toJ2000(JulianDay);

        double obliquityOfTheEcliptic = Math.toRadians(23.4397);
        double L = MathUtil.normalzeAngle(Math.toRadians(218.316) + Math.toRadians(13.176396) * j2000);
        double M = MathUtil.normalzeAngle(Math.toRadians(134.963) + Math.toRadians(13.064993) * j2000);
        double F = MathUtil.normalzeAngle(Math.toRadians(93.272) + Math.toRadians(13.229350) * j2000);

        double elipticalLongitude = L + Math.toRadians(6.289) * Math.sin(M); //lambda
        double elipticalLatitude = Math.toRadians(5.128) * Math.sin(F); // beta
        double distanceToMoonInKm = 385001 - 20905 * Math.cos(M); // delta


        double declination = Math.asin(Math.sin(elipticalLatitude) * Math.cos(obliquityOfTheEcliptic) + Math.cos(elipticalLatitude) * Math.sin(obliquityOfTheEcliptic) * Math.sin(elipticalLongitude));
        double rightAscension = Math.atan2(Math.sin(elipticalLongitude) * Math.cos(obliquityOfTheEcliptic) - Math.tan(elipticalLatitude) * Math.sin(obliquityOfTheEcliptic), Math.cos(elipticalLongitude));

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
