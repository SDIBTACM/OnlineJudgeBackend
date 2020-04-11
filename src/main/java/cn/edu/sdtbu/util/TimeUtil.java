package cn.edu.sdtbu.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * time util
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:27
 */

public class TimeUtil {
    private static final long MILL_TO_SECOND = 1000;
    private static final long SECOND_TO_MINUTE = 60;
    private static final long MINUTE_TO_HOUR = 60;
    private static final long HOUR_TO_DAY = 24;
    public static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static Timestamp add(Timestamp date, long time, TimeUnit timeUnit) {
        return new Timestamp(date.getTime() + time2Mill(time, timeUnit));
    }

    /**
     * @param times     time
     * @param timeUnit  unit
     * @return          millisecond
     */
    public static long time2Mill(long times, TimeUnit timeUnit) {

        //really don't have 'break' keyword...
        switch (timeUnit) {
            case DAYS:    times *= HOUR_TO_DAY;
            case HOURS:   times *= MINUTE_TO_HOUR;
            case MINUTES: times *= SECOND_TO_MINUTE;
            case SECONDS: times *= MILL_TO_SECOND;
            case MILLISECONDS:
            default:
        }
        return times;
    }

    private TimeUtil(){}
}

