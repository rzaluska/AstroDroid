package io.github.luxurypro.astrodroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeManager {
    private Instant previousTickTime;
    private DateTime currentDate;
    private boolean stop;

    public TimeManager() {
        previousTickTime = new Instant();
        currentDate = new DateTime().withZone(DateTimeZone.UTC);
        stop = false;
    }

    public synchronized DateTime getCurrentDateTimeUTC() {
        return new DateTime(this.currentDate);
    }

    public synchronized void tick(long rate) throws InterruptedException {
        if (stop)
            return;
        Instant instantNow = new Instant();
        Interval delta = new Interval(previousTickTime, instantNow);
        Duration duration = delta.toDuration();
        long milis = duration.getMillis();
        long scaledDuration = milis * rate;
        previousTickTime = instantNow;
        currentDate = currentDate.withDurationAdded(scaledDuration, 1);
    }

    public synchronized double getJulianDate() {
        GregorianCalendar gregorianCalendar = currentDate.toGregorianCalendar();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DateUtil.toJulianDay(currentDate.toGregorianCalendar());
    }

    public synchronized void resetToCurrentTimeDate() {
        currentDate = new DateTime().withZone(DateTimeZone.UTC);
    }

    public void setCurrentDateTime(Calendar currentDateTime) {
        currentDate = new DateTime(currentDateTime).withZone(DateTimeZone.UTC);
    }

    public synchronized void stop() {
        this.stop = true;
    }

    public synchronized void resume() {
        this.stop = false;
    }
}
