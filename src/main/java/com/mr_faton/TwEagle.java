package com.mr_faton;

import com.mr_faton.core.TaskControl;
import org.apache.log4j.Logger;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class TwEagle {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.TwEagle");
    private Thread executableThread;
    private final TaskControl taskControl;

    public TwEagle(TaskControl taskControl) {
        logger.debug("constructor");
        this.taskControl = taskControl;
    }

    public void start() {
        logger.info("start Task Control");
        taskControl.setState(true);
        executableThread = new Thread(taskControl);
        executableThread.start();
    }

    public void stop() {
        logger.info("stop Task Control");
        if (executableThread != null) {
            taskControl.setState(false);
            executableThread.interrupt();
        }
    }

    public void shutDown() {
        stop();
        taskControl.shutDown();
    }
}
