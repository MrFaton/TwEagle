package com.mr_faton.core.pool.db_connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public interface DBConnectionPool {
    Connection getConnection() throws SQLException;
    void shutDown();
}
