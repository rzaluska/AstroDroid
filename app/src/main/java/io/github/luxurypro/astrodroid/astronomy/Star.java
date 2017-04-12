package io.github.luxurypro.astrodroid.astronomy;

public class Star {
    private EquatorialCoordinates equatorialCoordinates;

    public Star(double rightAscension, double declination) {
        this.equatorialCoordinates = new EquatorialCoordinates(rightAscension, declination);
    }

    public HorizontalCoordinates getHorizontalCoordinates(double j2000, double latitude, double longitude) {
        return HorizontalCoordinates.fromEquatorialCoordinates(this.equatorialCoordinates, j2000, latitude, longitude);
    }
}
