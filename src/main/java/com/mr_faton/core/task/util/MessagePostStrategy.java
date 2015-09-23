package com.mr_faton.core.task.util;

import com.mr_faton.core.util.RandomGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 23.09.2015
 */
public class MessagePostStrategy {
    private static final Map<String, Map<String, Integer>> strategyHolder = new HashMap<>();

    private static final String DAY_PERCENT = "dayPercent";
    private static final String STRATEGY_PERCENT = "strategyPercent";
    private static final String START_TIME = "startTime";
    private static final String STOP_TIME = "stopTime";

    private static final int MIN_DAY_PERCENT = 5;
    private static final int MAX_DAY_PERCENT = 80;

    private static final int MIN_STRATEGY_PERCENT = 5;
    private static final int MAX_STRATEGY_PERCENT = 100;

    public static long getStartTimeForUser()



    public static synchronized void evalStrategyForUser(String userName) {
        int dayPercent = RandomGenerator.getNumber(MIN_DAY_PERCENT, MAX_DAY_PERCENT);
        int strategyPercent = RandomGenerator.getNumber(MIN_STRATEGY_PERCENT, MAX_STRATEGY_PERCENT);

        Map<String, Integer> parameters = new HashMap<>(2, 1f);
        parameters.put(DAY_PERCENT, dayPercent);
        parameters.put(STRATEGY_PERCENT, strategyPercent);

        strategyHolder.put(userName, parameters);
    }

    public static int getDayPercent(String userName) {
        return strategyHolder.get(userName).get(DAY_PERCENT);
    }

    public static int getStrategyPercent(String userName) {
        return strategyHolder.get(userName).get(STRATEGY_PERCENT);

    }
}
