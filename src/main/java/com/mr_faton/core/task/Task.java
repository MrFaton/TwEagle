package com.mr_faton.core.task;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public interface Task {
    boolean getStatus();
    void setStatus(boolean status);

    long getTime();

    void update();
    void save();

    void execute();

    void setDailyParams();
}
