package io.github.luxurypro.astrodroid;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import io.github.luxurypro.astrodroid.astronomy.Moon;
import io.github.luxurypro.astrodroid.astronomy.Sun;

public class SunTest {
    @Test
    public void getAzimunt() throws Exception {
        double julianDay = 2453097;
        Sun sun = new Sun(julianDay, Math.toRadians(52), Math.toRadians(5));
    }

    @Test
    public void getAzimuntNext() throws Exception {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        double julianDay = DateUtil.toJulianDay(date);
        Sun sun = new Sun(julianDay, Math.toRadians(52.22970), Math.toRadians(21.01220));
    }

    @Test
    public void getMoon() throws Exception {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        date.set(2017, 0, 1, 12, 0);
        double julianDay = DateUtil.toJulianDay(date);
        Moon moon = new Moon(julianDay, Math.toRadians(52.22970), Math.toRadians(21.01220));
    }

    @Test
    public void testJulianDay() {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        date.set(2004, 3, 1, 12, 0);
        double julianDay = DateUtil.toJulianDay(date);
        Assert.assertEquals(2453097.0, julianDay, 0.01);

        Calendar testDate = DateUtil.julianDayToCalendar(2453097.0);
        Assert.assertEquals(testDate.get(Calendar.YEAR), 2004);
        Assert.assertEquals(testDate.get(Calendar.MONTH), 3);
        Assert.assertEquals(testDate.get(Calendar.DAY_OF_MONTH), 1);
        Assert.assertEquals(testDate.get(Calendar.HOUR_OF_DAY), 12);
        Assert.assertEquals(testDate.get(Calendar.MINUTE), 0);


        date.set(1858, 10, 16, 12, 0);
        julianDay = DateUtil.toJulianDay(date);
        Assert.assertEquals(2400000.0, julianDay, 0.01);

        date.set(1970, 0, 1, 0, 0);
        julianDay = DateUtil.toJulianDay(date);
        Assert.assertEquals(2440587.5, julianDay, 0.01);
    }

}