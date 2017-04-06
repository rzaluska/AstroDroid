package io.github.luxurypro.astrodroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class SkyView extends View {
    private Paint paint;
    private double altitude;
    private double azimunt;
    private int progress;

    public SkyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius = Math.min(x / 2, y / 2) - 16;
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(Color.WHITE);
        canvas.drawPaint(this.paint);
        this.paint.setColor(Color.parseColor("#5194ff"));
        canvas.drawCircle(x / 2, y / 2, radius, paint);
        this.paint.setColor(Color.YELLOW);
        double r = radius * Math.cos(altitude);
        double alpha = azimunt - Math.PI/2;
        int sx = (int) (r * Math.cos(alpha));
        int sy = (int) (r * Math.sin(alpha));
        canvas.drawCircle(x/2 + sx, y/2 - sy, (float) (radius * 0.1), paint);
    }

    public void setData(double azimunt, double altitude) {
        this.azimunt = azimunt;
        this.altitude = altitude;
        this.invalidate();
    }
}
