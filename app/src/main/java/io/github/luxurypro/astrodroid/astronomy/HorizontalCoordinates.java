package io.github.luxurypro.astrodroid.astronomy;

import io.github.luxurypro.astrodroid.DateUtil;
import io.github.luxurypro.astrodroid.MathUtil;

import static java.lang.Math.*;

public class HorizontalCoordinates {
    private double azimunt;
    private double altitude;

    public HorizontalCoordinates(double azimunt, double altitude) {
        this.azimunt = azimunt;
        this.altitude = altitude;
    }

    public double getAzimunt() {
        return azimunt;
    }

    public void setAzimunt(double azimunt) {
        this.azimunt = azimunt;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public static HorizontalCoordinates fromEquatorialCoordinates(EquatorialCoordinates equatorialCoordinates, double j2000, double latitude, double longitude) {
        double siderialTime = DateUtil.getEarthLocalSiderialTime(j2000, longitude);

        double hourAngle = siderialTime - equatorialCoordinates.getRightAscension();
        double azimunt = MathUtil.normalzeAngle(atan2(sin(hourAngle), (cos(hourAngle) * sin(latitude)) - (tan(equatorialCoordinates.getDeclination()) * cos(latitude))));
        double altitude = asin(sin(latitude) * sin(equatorialCoordinates.getDeclination()) + cos(latitude) * cos(equatorialCoordinates.getDeclination()) * cos(hourAngle));
        return new HorizontalCoordinates(azimunt, altitude);
    }
}
