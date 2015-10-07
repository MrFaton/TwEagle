package com.mr_faton.core.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.0
 */
public class AppContext {
    private static final ApplicationContext appCtx = new ClassPathXmlApplicationContext("TwEagleConfig.xml");

    public static final Object getBeanByName(String beanName) {
        return appCtx.getBean(beanName);
    }
}
