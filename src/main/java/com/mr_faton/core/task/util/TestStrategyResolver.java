package com.mr_faton.core.task.util;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 22.09.2015
 */
public class TestStrategyResolver {
    public static void main(String[] args) {
        String[] nameArr = {"Mike", "Anna", "Luci", "Judi", "Andy"};
        for (String name : nameArr) {
            System.out.println(name);
            System.out.println("part of the day=" + StrategyResolver.getPartOfTheDay(name));
            System.out.println("strategy index=" + StrategyResolver.getStrategyIndex(name));
            System.out.println("");
        }
    }
}
