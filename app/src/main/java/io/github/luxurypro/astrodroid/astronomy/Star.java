package io.github.luxurypro.astrodroid.astronomy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Star implements AstroObject {
    private EquatorialCoordinates equatorialCoordinates;
    private HorizontalCoordinates horizontalCoordinates;

    public Star(double rightAscension, double declination) {
        this.equatorialCoordinates = new EquatorialCoordinates(rightAscension, declination);
    }

    public HorizontalCoordinates getHorizontalCoordinates(double j2000, double latitude, double longitude) {
        return HorizontalCoordinates.fromEquatorialCoordinates(this.equatorialCoordinates, j2000, latitude, longitude);
    }

    @Override
    public void updatePosition(double j2000, double latitude, double longitude) {
        this.horizontalCoordinates = HorizontalCoordinates.fromEquatorialCoordinates(this.equatorialCoordinates, j2000, latitude, longitude);
    }

    @Override
    public double getAzimunt() {
        return this.horizontalCoordinates.getAzimunt();
    }

    @Override
    public double getAltitude() {
        return this.horizontalCoordinates.getAltitude();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, double radius, Point middle) {
        if (this.getAltitude() < 0)
            return;
        paint.setColor(Color.WHITE);
        double r = radius * Math.tan((Math.PI / 2 - this.getAltitude()) / 2);
        //double r = radius * ((Math.PI / 2) - this.getAltitude()) / (Math.PI / 2);
        double alpha = this.getAzimunt() + Math.PI / 2;
        int sx = (int) (r * Math.cos(alpha));
        int sy = (int) (r * Math.sin(alpha));
        canvas.drawCircle(middle.x + sx, middle.y + sy, (float) (radius * 0.01), paint);
    }
}
