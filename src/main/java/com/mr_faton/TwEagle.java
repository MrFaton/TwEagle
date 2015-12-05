package com.mr_faton;

import com.mr_faton.core.TaskScheduler;
import org.apache.log4j.Logger;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 */
public class TwEagle {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.TwEagle");
    private Thread executableThread;
    private final TaskScheduler taskScheduler;

    public TwEagle(TaskScheduler taskScheduler) {
        logger.debug("constructor");
        this.taskScheduler = taskScheduler;
    }

    public void start() {
        logger.info("start TaskControl");
        taskScheduler.setState(true);
        executableThread = new Thread(taskScheduler);
        executableThread.start();
    }

    public void stop() {
        logger.info("stop TaskControl");
        if (executableThread != null) {
            taskScheduler.setState(false);
            executableThread.interrupt();
        }
    }

    public void shutDown() {
        stop();
        taskScheduler.shutDown();
    }
}
