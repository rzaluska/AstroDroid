package io.github.luxurypro.astrodroid.astronomy;

class ElipticalCoordinates {
    private double latitude;
    private double longitude;

    public ElipticalCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getBeta() {
        return getLatitude();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLamda() {
        return getLongitude();
    }
}
