package com.mr_faton.core.task.util;

import com.mr_faton.core.util.RandomGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 22.09.2015
 */
public class StrategyResolver {
    private static final Map<String, Map<String, Integer>> strategyHolder = new HashMap<>();
    private static final String PART_OF_THE_DAY = "partOfTheDay";
    private static final String STRATEGY_INDEX = "strategyIndex";

    public static void clearHolder() {
        strategyHolder.clear();
    }

    public static int getPartOfTheDay(String userName) {
        if (!strategyHolder.containsKey(userName)) strategyHolder.put(userName, evalParameters());
        return strategyHolder.get(userName).get(PART_OF_THE_DAY);
    }

    public static int getStrategyIndex(String userName) {
        if (!strategyHolder.containsKey(userName)) strategyHolder.put(userName, evalParameters());
        return strategyHolder.get(userName).get(STRATEGY_INDEX);
    }






    private static Map<String, Integer> evalParameters() {
        int partOfTheDayIndex = evalPartOfTheDay();
        int strategyIndex = evalStrategyIndex();

        Map<String, Integer> userParameters = new HashMap<>();
        userParameters.put(PART_OF_THE_DAY, partOfTheDayIndex);
        userParameters.put(STRATEGY_INDEX, strategyIndex);
        return userParameters;
    }

    private static int evalPartOfTheDay() {
        return RandomGenerator.getNumberFromZeroToRequirement(5); //5 - all parts of the day
    }

    private static int evalStrategyIndex() {
        int rndPercent = RandomGenerator.getNumberFromZeroToRequirement(100); //max percent
        System.out.println(rndPercent);
        if (rndPercent >= 0 && rndPercent <= PostStrategy.QUICK_FAST_PERCENT) {
            return PostStrategy.QUICK_FAST;
        } else if (rndPercent > PostStrategy.QUICK_FAST_PERCENT && rndPercent <= PostStrategy.FAST_PERCENT) {
            return PostStrategy.FAST;
        } else if (rndPercent > PostStrategy.FAST_PERCENT && rndPercent <= PostStrategy.QUICK_MEDIUM_PERCENT) {
            return PostStrategy.QUICK_MEDIUM;
        } else if (rndPercent > PostStrategy.QUICK_MEDIUM_PERCENT && rndPercent <= PostStrategy.MEDIUM_PERCENT) {
            return PostStrategy.MEDIUM;
        } else if (rndPercent > PostStrategy.MEDIUM_PERCENT && rndPercent <= PostStrategy.QUICK_SLOW_PERCENT) {
            return PostStrategy.QUICK_SLOW;
        } else if (rndPercent > PostStrategy.QUICK_SLOW_PERCENT && rndPercent <= PostStrategy.SLOW_PERCENT) {
            return PostStrategy.SLOW;
        }
        System.err.println("this could not happen");
        return PostStrategy.MEDIUM;
    }
}
