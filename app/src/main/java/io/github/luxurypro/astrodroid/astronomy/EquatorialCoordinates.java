package io.github.luxurypro.astrodroid.astronomy;

import static java.lang.Math.*;

public class EquatorialCoordinates {
    private double rightAscension;
    private double declination;

    public EquatorialCoordinates(double rightAscension, double declination) {
        this.rightAscension = rightAscension;
        this.declination = declination;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(double rightAscension) {
        this.rightAscension = rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    public void setDeclination(double declination) {
        this.declination = declination;
    }

    public static double getEarthobliquityOfTheEcliptic(double j2000) {
        return toRadians(23.439) - toRadians(0.0000004) * j2000;
    }

    public static EquatorialCoordinates fromeElipticalCoordinates(ElipticalCoordinates elipticalCoordinates, double j2000) {
        double obliquityOfTheEcliptic = getEarthobliquityOfTheEcliptic(j2000);
        double declination = asin(Math.sin(elipticalCoordinates.getLatitude()) * cos(obliquityOfTheEcliptic) + cos(elipticalCoordinates.getLatitude()) * sin(obliquityOfTheEcliptic) * sin(elipticalCoordinates.getLongitude()));
        double rightAscension = atan2(sin(elipticalCoordinates.getLongitude()) * cos(obliquityOfTheEcliptic) - tan(elipticalCoordinates.getLatitude()) * sin(obliquityOfTheEcliptic), cos(elipticalCoordinates.getLongitude()));
        return new EquatorialCoordinates(rightAscension, declination);
    }
}
