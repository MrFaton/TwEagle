package com.mr_faton.core.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 16.09.2015.
 */
public class TimeWizard {
    public static String convertToFuture(long sleepTime) {
        Date date = new Date(System.currentTimeMillis() + sleepTime);
        return String.format("%tH:%<tM:%<tS", date);
    }

    public static String convertToIdle(long sleepTime) {
        long s = (sleepTime / 1000) % 60;
        long m = (sleepTime / 1000 / 60) % 60;
        long h = (sleepTime / 1000 / 60 / 60) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }

    public static int getCurDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /*TODO done*/
    public static int monthDiff(Calendar startCalendar) {
        Calendar currentCalendar = Calendar.getInstance();
        int diffYear = currentCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear * 12 + currentCalendar.get(Calendar.MONTH)
    }
}
