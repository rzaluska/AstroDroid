package io.github.luxurypro.astrodroid;

public class LowPassFilter {
    private double smoothingFactor = 0.9;
    private double lastSin = 0;
    private double lastCos = 0;

    public LowPassFilter(double value) {
        lastSin = Math.sin(value);
        lastCos = Math.cos(value);
    }

    public void update(double value) {
        lastSin = smoothingFactor * lastSin + (1 - smoothingFactor) * Math.sin(value);
        lastCos = smoothingFactor * lastCos + (1 - smoothingFactor) * Math.cos(value);
    }

    public double getValue() {
        return Math.atan2(lastSin, lastCos);
    }
}
