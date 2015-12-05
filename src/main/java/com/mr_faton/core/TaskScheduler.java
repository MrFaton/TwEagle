package com.mr_faton.core;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.execution_pool.ExecutionPool;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.5
 */
public class TaskScheduler implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TaskScheduler");

    private static int APP_START_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_START_HOUR"));
    private static int APP_STOP_HOUR = Integer.valueOf(SettingsHolder.getSetupByKey("APP_STOP_HOUR"));

    private final List<Task> taskList;
    private boolean state = true;


    private final ExecutionPool executionPool;
//    private final TransactionManager transactionManager;

    public TaskScheduler(ExecutionPool executionPool, /*TransactionManager transactionManager,*/ List<Task> taskList) {
        logger.debug("constructor");
        this.executionPool = executionPool;
//        this.transactionManager = transactionManager;
        this.taskList = taskList;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        logger.debug("status changed to " + state);
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
                logger.info("next task is " + task);

                taskTime = task.getTime();
                sleepTime = taskTime - System.currentTimeMillis();
                if (sleepTime > 0) {
                    idle(sleepTime);
                }

                task.setNextTime();
                executionPool.execute(task);

                if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
            }

        } catch (InterruptedException ex) {
            logger.info("Task Control was interrupted");
        } catch (Exception e) {
            logger.warn("exception during executing task", e);
            state = false;
        }
    }




    private void setDailyParams() throws Exception {
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                for (Task task : taskList) {
//                    task.setDailyParams();
//                }
//            }
//        });
    }

    private void updateAllTasks() throws Exception {
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                for (Task task : taskList) {
//                    task.update();
//                }
//            }
//        });
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
//        logger.info("the next task will be executed in " + TimeWizard.convertToFuture(sleepTime) + ", " +
//                "idle period is " + TimeWizard.convertToIdle(sleepTime));
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, APP_STOP_HOUR);
        long endOfWorkDay = calendar.getTimeInMillis();
        if (nextTask == null || minTaskTime == Long.MAX_VALUE || nextTask.getTime() >= endOfWorkDay)
            throw new NoSuchEntityException();
        return nextTask;
    }

    public void shutDown() {
//        transactionManager.shutDown();
        executionPool.shutDown();
    }
}
