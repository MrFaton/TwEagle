package com.mr_faton;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.util.SettingsHolder;

import java.io.IOException;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class Start {
    public static void main(String[] args) throws IOException {
        SettingsHolder.loadSettings();
        TwEagle twEagle = (TwEagle) AppContext.getBeanByName("twEagle");
        twEagle.start();
    }
}
