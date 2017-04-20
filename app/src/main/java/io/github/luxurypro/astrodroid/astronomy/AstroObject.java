package io.github.luxurypro.astrodroid.astronomy;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import io.github.luxurypro.astrodroid.MathUtil;

import static java.lang.Math.cos;

public interface AstroObject {
    void updatePosition(double j2000, double latitude, double longitude);

    public double getAzimunt();

    public double getAltitude();

    void draw(Canvas canvas, Paint paint, double radius, Point middle);
}
