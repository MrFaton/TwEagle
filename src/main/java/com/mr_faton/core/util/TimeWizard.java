package com.mr_faton.core.util;

import java.util.Calendar;

/**
 * Created by root on 16.09.2015.
 */
public class TimeWizard {
//    @Deprecated
//    public static String convertToFuture(long sleepTime) {
//        Date date = new Date(System.currentTimeMillis() + sleepTime);
//        return String.format("%tH:%<tM:%<tS", date);
//    }
//    @Deprecated
//    public static String convertToIdle(long sleepTime) {
//        long s = (sleepTime / 1000) % 60;
//        long m = (sleepTime / 1000 / 60) % 60;
//        long h = (sleepTime / 1000 / 60 / 60) % 24;
//        return String.format("%02d:%02d:%02d", h,m,s);
//    }

    public static String formatDateWithTime(long dateTime) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        return dateFormat.format(new Date(dateTime));
        return String.format("%td-%<tm-%<tY %<tH:%<tM:%<tS", dateTime);
    }

    public static String formatDate(long date) {
        return String.format("%td-%<tm-%<tY", date);
    }

    public static int getCurDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int monthDiff(Calendar startCalendar) {
        Calendar curCalendar = Calendar.getInstance();

        int startCalendarDays = (startCalendar.get(Calendar.YEAR) * 365) + ((startCalendar.get(Calendar.MONTH) + 1) * 30) + startCalendar.get(Calendar.DAY_OF_MONTH);
        int curCalendarDays = (curCalendar.get(Calendar.YEAR) * 365) + ((curCalendar.get(Calendar.MONTH) + 1) * 30) + curCalendar.get(Calendar.DAY_OF_MONTH);

        int diffDays = curCalendarDays - startCalendarDays;

        return diffDays / 30;
    }
}
