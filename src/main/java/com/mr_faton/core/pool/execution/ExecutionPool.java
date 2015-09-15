package com.mr_faton.core.pool.execution;

import com.mr_faton.core.task.Task;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public interface ExecutionPool {
    void execute(Task task) throws Exception;
    void shutDown();
}
