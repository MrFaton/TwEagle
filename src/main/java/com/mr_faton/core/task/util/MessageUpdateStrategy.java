package com.mr_faton.core.task.util;

import com.mr_faton.core.util.RandomGenerator;
import com.mr_faton.core.util.SettingsHolder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 23.09.2015
 */
public class MessageUpdateStrategy {
    private static final Map<String, Map<String, Long>> strategyHolder = new HashMap<>();

    private static final String START_TIME = "startTime";
    private static final String STOP_TIME = "stopTime";

    private static final int MIN_DAY_PERCENT = 5;
    private static final int MAX_DAY_PERCENT = 80;

    private static final int MIN_SLOWDOWN_PERCENT = 25;
    private static final int MAX_SLOWDOWN_PERCENT = 100;

    public static long getStartTimeForUser(String userName) {
        if (!strategyHolder.containsKey(userName)) evalStrategyForUser(userName);
        return strategyHolder.get(userName).get(START_TIME);
    }

    public static long getStopTimeForUser(String userName) {
        if (!strategyHolder.containsKey(userName)) evalStrategyForUser(userName);
        return strategyHolder.get(userName).get(STOP_TIME);
    }



    private static synchronized void evalStrategyForUser(String userName) {
        if (strategyHolder.containsKey(userName)) return;

        int dayPercent = RandomGenerator.getNumber(MIN_DAY_PERCENT, MAX_DAY_PERCENT);
        int slowdownPercent = RandomGenerator.getNumber(MIN_SLOWDOWN_PERCENT, MAX_SLOWDOWN_PERCENT);

        Calendar appStartTime = Calendar.getInstance();
        appStartTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(SettingsHolder.getSetupByKey("APP_START_HOUR")));
        appStartTime.set(Calendar.MINUTE, 0);

        Calendar appStopTime = Calendar.getInstance();
        appStopTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(SettingsHolder.getSetupByKey("APP_STOP_HOUR")));
        appStopTime.set(Calendar.MINUTE, 0);

        long startUserTime = evalStartTime(appStartTime.getTimeInMillis(), appStopTime.getTimeInMillis(), dayPercent);
        long stopUserTime = evalStopTime(startUserTime, appStopTime.getTimeInMillis(), slowdownPercent);

        Map<String, Long> timeParameters = new HashMap<>(2, 1f);
        timeParameters.put(START_TIME, startUserTime);
        timeParameters.put(STOP_TIME, stopUserTime);

        strategyHolder.put(userName, timeParameters);
    }

    private static long evalStartTime(long appStartTime, long appStopTime, int dayPercent) {
        long totalWorkPeriod = appStopTime - appStartTime;
        long offset =  totalWorkPeriod * dayPercent / 100;
        return appStartTime + offset;
    }

    private static long evalStopTime(long userStartTime, long appStopTime, int slowdownPercent) {
        long workPeriod = appStopTime - userStartTime;
        long offset = workPeriod * slowdownPercent / 100;
        return userStartTime + offset;
    }

}
/*TODO test and debug this class*/