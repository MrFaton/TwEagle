package com.mr_faton.core.task;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public interface Task extends Runnable {
    boolean getStatus();
    void setStatus(boolean status);

    long getTime();
    void setTime();

    void update();

    void setDailyParams();
}
