package com.mr_faton;

import com.mr_faton.core.TaskManager;
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
    private final TaskManager taskManager;

    public TwEagle(TaskManager taskManager) {
        logger.debug("constructor");
        this.taskManager = taskManager;
    }

    public void start() {
        logger.info("start TaskControl");
        taskManager.setState(true);
        executableThread = new Thread(taskManager);
        executableThread.start();
    }

    public void stop() {
        logger.info("stop TaskControl");
        if (executableThread != null) {
            taskManager.setState(false);
            executableThread.interrupt();
        }
    }

    public void shutDown() {
        stop();
        taskManager.shutDown();
    }
}
