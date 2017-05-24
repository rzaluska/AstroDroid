package io.github.luxurypro.astrodroid.astronomy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import io.github.luxurypro.astrodroid.util.MathUtil;

public class SkyMap {
    private List<AstroObject> astroObjects;

    public SkyMap() {
        astroObjects = new LinkedList<>();
    }

    public void updatePosition(double j2000, double latitude, double longitude) {
        for (AstroObject astroObject : astroObjects) {
            astroObject.updatePosition(j2000, latitude, longitude);
        }
    }

    public void draw(Canvas canvas, Paint paint, double radius, Point middle) {
        for (AstroObject astroObject : astroObjects) {
            astroObject.draw(canvas, paint, radius, middle);
        }
    }

    public static SkyMap readFromStream(InputStream inputStream) throws IOException, JSONException {
        SkyMap skyMap = new SkyMap();
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String json = new String(buffer, "UTF-8");
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject star = jsonArray.getJSONObject(i);
            String rightAscension = star.getString("ra");
            String[] elems = rightAscension.split(",");
            int rightAscensionHour = Integer.parseInt(elems[0]);
            int rightAscensionMinute = Integer.parseInt(elems[1]);
            double rightAscensionSecond = Double.parseDouble(elems[2]);
            double rightAscensionAngle = MathUtil.radiansFromHourMinSec(rightAscensionHour, rightAscensionMinute, rightAscensionSecond);
            String declination = star.getString("dec");
            String[] decElems = declination.split(",");
            int declinationDeg = Integer.parseInt(decElems[0]);
            int declinationMinute = Integer.parseInt(decElems[1]);
            double declinationSecond = Double.parseDouble(decElems[2]);
            double declinationAngle = MathUtil.radiansFromDegMinSec(declinationDeg, declinationMinute, declinationSecond);
            Star starObject = new Star(rightAscensionAngle, declinationAngle);
            skyMap.astroObjects.add(starObject);
        }

        return skyMap;
    }
}
