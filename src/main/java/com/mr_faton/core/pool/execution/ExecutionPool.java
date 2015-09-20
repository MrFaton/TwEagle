package com.mr_faton.core.pool.execution;

import com.mr_faton.core.task.Task;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.1
 */
public interface ExecutionPool {
    void execute(Task task) throws Exception;
    void shutDown();
}
