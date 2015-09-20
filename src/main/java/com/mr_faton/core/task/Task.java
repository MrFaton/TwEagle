package com.mr_faton.core.task;

import java.sql.SQLException;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public interface Task {
    boolean getStatus();
    void setStatus(boolean status);

    long getTime();
    void setNextTime();

    void update() throws SQLException;
    void save() throws SQLException;

    void execute();

    void setDailyParams() throws SQLException;
}
