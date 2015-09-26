package com.mr_faton.core.task.util;

import com.mr_faton.core.util.SettingsHolder;

import java.io.IOException;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 22.09.2015
 */
public class TestMessageUpdateStrategy {
    public static void main(String[] args) throws IOException {
        SettingsHolder.loadSettings();
        String[] nameArr = {"Mike", "Anna", "Luci", "Judi", "Andy"};
        for (String name : nameArr) {
            long from = MessageUpdateStrategy.getStartTimeForUser(name);
            long to = MessageUpdateStrategy.getStopTimeForUser(name);
            long total = (to - from) / 1000 / 60;
            System.out.println(name + " => " + from + " - " + to + " = " + total + " total min");
        }
    }
}
