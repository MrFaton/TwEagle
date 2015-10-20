package com.mr_faton.core.pool.execution.impl;

import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.execution.ExecutionPool;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.Command;
import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.0
 */
public class OptimalExecutor implements ExecutionPool {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.pool.execution.impl.OptimalExecutor");
    private ExecutorService pool;
    private BlockingQueue<Runnable> queue;
    private static final int QUEUE_SIZE = 2;
    private static final int MAX_POOL_SIZE = 10;
    private static final int ROOT_THREADS = 1;
    private static final long THREAD_LIFE_TIME = 2L;
    private final TransactionManager transactionManager;

    public OptimalExecutor(TransactionManager transactionManager) {
        logger.debug("constructor");
        queue = new ArrayBlockingQueue<>(QUEUE_SIZE, true);
        pool =
                new ThreadPoolExecutor(ROOT_THREADS, MAX_POOL_SIZE, THREAD_LIFE_TIME, TimeUnit.MINUTES, queue);
        this.transactionManager = transactionManager;
    }

    @Override
    public void execute(final Task task) {
        logger.debug("execute " + task);

        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    transactionManager.doInTransaction(new Command() {
                        @Override
                        public void doCommands() throws Exception {
                            task.execute();
                            task.save();
                            task.update();
                        }
                    });
                } catch (Exception e) {
                    logger.warn("exception during executing task", e);
                }
            }
        });
    }

    @Override
    public void execute(final Command command) {
        logger.debug("execute command");
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.doCommands();
                } catch (Exception e) {
                    logger.warn("exception during executing command", e);
                }
            }
        });
    }

    @Override
    public void shutDown() {
        logger.debug("shut down OptimalExecutor");
        if (pool != null) {
            pool.shutdown();
        }
    }

    int getLargestPoolSize() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) pool;
        return threadPoolExecutor.getLargestPoolSize();
    }
}
