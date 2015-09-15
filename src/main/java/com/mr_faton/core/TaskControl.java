package com.mr_faton.core;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.pool.execution.ExecutionPool;
import com.mr_faton.core.task.Task;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class TaskControl implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TaskControl");
    private static final List<Task> taskList = new ArrayList<>();
    private final ExecutionPool executionPool;
    private boolean state = true;


    public TaskControl(ExecutionPool executionPool) {
        logger.debug("constructor");
        this.executionPool = executionPool;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public void run() {
        for (Task task : taskList) {
            task.setDailyParams();
        }
        while (state) {
            Task task;
            long taskTime;
            long sleepTime;
            try {
                task = getNextTask();

                taskTime = task.getTime();
                sleepTime = taskTime - System.currentTimeMillis();
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }

                task.setTime();
                executionPool.execute(task);

            } catch (Exception ex) {
                state = false;
                break;
            }

        }
    }

    private Task getNextTask() throws NoSuchEntityException {
        Task nextTask = null;
        long minTaskTime = Long.MAX_VALUE;
        long taskTime;
        for (Task task : taskList) {
            taskTime = task.getTime();
            if (minTaskTime >= taskTime) {
                minTaskTime = taskTime;
                nextTask = task;
            }
        }
        if (nextTask == null) throw new NoSuchEntityException();
        return nextTask;
    }

    public static void addTaskToList(Task task) {
        taskList.add(task);
    }

}
