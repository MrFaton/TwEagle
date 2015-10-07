package com.mr_faton.core;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.execution.ExecutionPool;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.5
 */
public class TaskManager implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TaskManager");
    private static final List<Task> taskList = new ArrayList<>();
    private boolean state = true;
    private static int APP_START_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_START_HOUR"));
    private static int APP_STOP_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_STOP_HOUR"));

    private final ExecutionPool executionPool;
    private final TransactionManager transactionManager;

    public TaskManager(ExecutionPool executionPool, TransactionManager transactionManager) {
        this.executionPool = executionPool;
        this.transactionManager = transactionManager;
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

            setDailyParams();
            updateAllTasks();

            Task task;
            long taskTime;
            long sleepTime;

            while (state) {
                logger.debug("new loop");
                int curHour = TimeWizard.getCurHour();
                if (curHour <= APP_START_HOUR && curHour >= APP_STOP_HOUR) {
                    logger.info("out of work period");
                    idle(getSleepTimeForNextPeriod());

                    setDailyParams();
                    updateAllTasks();
                }

                try {
                    task = getNextTask();
                } catch (NoSuchEntityException noEntityEx) {
                    logger.info("no task to execute");
                    idle(getSleepTimeForNextPeriod());
                    continue;
                }


                taskTime = task.getTime();
                sleepTime = taskTime - System.currentTimeMillis();
                if (sleepTime > 0) {
                    idle(sleepTime);
                }

                task.setNextTime();
                executionPool.execute(task);
            }

        } catch (InterruptedException ex) {
            logger.info("Task Control was interrupted");
        } catch (Exception e) {
            logger.warn("exception during executing task", e);
            state = false;
        }
    }





    private void updateAllTasks() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                for (Task task : taskList) {
                    task.update();
                }
            }
        });
    }

    private void setDailyParams() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                for (Task task : taskList) {
                    task.setDailyParams();
                }
            }
        });
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
        transactionManager.shutDown();
        executionPool.shutDown();
    }
}