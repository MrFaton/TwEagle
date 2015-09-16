package com.mr_faton.core;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.manager.TwitterUserManager;
import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.pool.execution.ExecutionPool;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class TaskControl implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TaskControl");
    private static final List<Task> taskList = new ArrayList<>();
    private final ExecutionPool executionPool;
    private final DBConnectionPool connectionPool;
    private final TwitterUserManager userManager;
    private boolean state = true;
    private static int APP_START_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_START_HOUR"));
    private static int APP_STOP_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_STOP_HOUR"));


    public TaskControl(ExecutionPool executionPool, DBConnectionPool connectionPool, TwitterUserManager userManager) throws SQLException {
        logger.debug("constructor");
        this.executionPool = executionPool;
        this.connectionPool = connectionPool;
        this.userManager = userManager;

        try {
            userManager.loadUserList();
        } catch (SQLException e) {
            logger.error("exception while loading user list from DB", e);
            throw e;
        }
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }







    @Override
    public void run() {
        try {
            int curHour = TimeWizard.getCurHour();
            if (curHour <= APP_START_HOUR && curHour >= APP_STOP_HOUR) {
                logger.info("out of work period");
                idle(getSleepTimeForNextPeriod());
            }

            setDailyParams();
            updateAllTasks();

            Task task;
            long taskTime;
            long sleepTime;

            while (state) {
                logger.debug("new loop");
                try {
                    task = getNextTask();
                } catch (NoSuchEntityException noEntityEx) {
                    logger.info("no task to execute");
                    idle(getSleepTimeForNextPeriod());
                    setDailyParams();
                    updateAllTasks();
                    continue;
                }


                taskTime = task.getTime();
                sleepTime = taskTime - System.currentTimeMillis();
                if (sleepTime > 0) {
                    idle(sleepTime);
                }

                task.setTime();
                executionPool.execute(task);
            }

        } catch (InterruptedException ex) {
            logger.info("Task Control was interrupted");
            stop();
        } catch (Exception e) {
            logger.warn("exception during executing task", e);
            stop();
        }
    }







    private void updateAllTasks() {
        for (Task task : taskList) {
            task.update();
        }
    }

    private void setDailyParams() {
        for (Task task : taskList) {
            task.setDailyParams();
        }
    }

    private long getSleepTimeForNextPeriod() {
        logger.debug("get sleep time for nex working period");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int start_hour = APP_START_HOUR;
        calendar.set(Calendar.HOUR_OF_DAY, start_hour);
        calendar.set(Calendar.MINUTE, 0);
//        System.out.println(String.format("Calendar: %td : %<tm : %<tY | %<tH : %<tM", calendar));
        long sleepTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        logger.debug("it's " + sleepTime);
        return (sleepTime);
    }

    private void idle(final long sleepTime) throws InterruptedException {
        logger.debug("sleep for " + TimeWizard.convertToIdle(sleepTime) + " " +
                "work begin in " + TimeWizard.convertToFuture(sleepTime));
        Thread.sleep(sleepTime);
    }

    private void stop() {
        state = false;
        try {
            userManager.saveUserList();
        } catch (SQLException e) {
            logger.error("exception while saving user list", e);
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
        if (nextTask == null || minTaskTime == Long.MAX_VALUE) throw new NoSuchEntityException();
        return nextTask;
    }

    public void addTaskToList(Task task) {
        taskList.add(task);
    }

    public void shutDown() {
        if (executionPool != null) executionPool.shutDown();
        if (connectionPool != null) connectionPool.shutDown();
    }
}
