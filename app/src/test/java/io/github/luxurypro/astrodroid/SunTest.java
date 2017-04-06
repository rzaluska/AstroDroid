package io.github.luxurypro.astrodroid;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SunTest {
    @Test
    public void getAzimunt() throws Exception {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        double julianDay = DateUtil.toJulianDay(c);
        double azimunt = Sun.getAzimunt(julianDay, Math.toRadians(52.229676), Math.toRadians(21.012229));
    }

}