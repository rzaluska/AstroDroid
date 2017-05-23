package io.github.luxurypro.astrodroid.astronomy;


import io.github.luxurypro.astrodroid.DateUtil;

public class CellestialBody {
    protected EquatorialCoordinates equatorialCoordinates;

    public EquatorialCoordinates getEquatorialCoordinates() {
        return this.equatorialCoordinates;
    }

    public double getTransit(double longitude) {
        double j2000 = DateUtil.getJ2000Now();
        double ra = this.getEquatorialCoordinates().getRightAscension();

        double siderialTime = DateUtil.getEarthLocalSiderialTime(j2000, longitude);

        while (Math.abs(siderialTime - ra) > 0.01) {
            if (siderialTime < ra)
                j2000 += 0.001;
            else
                j2000 -= 0.001;
            siderialTime = DateUtil.getEarthLocalSiderialTime(j2000, longitude);
        }
        return j2000;
    }

    public double getRise(double longitude, double latitude) {
        double Hhorizon = Math.acos(-Math.tan(latitude) * Math.tan(this.equatorialCoordinates.getDeclination())) / (2 * Math.PI);
        double transit = getTransit(longitude);
        return transit - Hhorizon;
    }

    public double getSet(double longitude, double latitude) {
        double Hhorizon = Math.acos(-Math.tan(latitude) * Math.tan(this.equatorialCoordinates.getDeclination())) / (2 * Math.PI);
        double transit = getTransit(longitude);
        return transit + Hhorizon;
    }
}
