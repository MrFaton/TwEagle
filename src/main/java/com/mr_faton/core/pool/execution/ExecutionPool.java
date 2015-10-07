package com.mr_faton.core.pool.execution;

import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.Command;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 * @version 1.1
 */
public interface ExecutionPool {
    void execute(Task task);
    void execute(Command command);
    void shutDown();
}
