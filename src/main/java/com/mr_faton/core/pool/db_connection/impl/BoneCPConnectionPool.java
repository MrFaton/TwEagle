package com.mr_faton.core.pool.db_connection.impl;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.util.SettingsHolder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class BoneCPConnectionPool implements DBConnectionPool {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.pool.db_connection.impl.BoneCPConnectionPool");
    private static final String JDBC_URL = SettingsHolder.getSetupByKey("JDBC_URL");
    private static final String JDBC_USER = SettingsHolder.getSetupByKey("JDBC_USER");
    private static final String JDBC_USER_PASSWORD = SettingsHolder.getSetupByKey("JDBC_USER_PASSWORD");
    private final BoneCP pool;

    public BoneCPConnectionPool() throws ClassNotFoundException, SQLException {
        logger.debug("constructor");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.warn("JDBC Driver class not found", e);
            throw e;
        }
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUser(JDBC_USER);
        config.setPassword(JDBC_USER_PASSWORD);
        config.setPartitionCount(2);
        config.setMinConnectionsPerPartition(1);
        config.setMaxConnectionsPerPartition(3);
        config.setAcquireIncrement(2);
        config.setAcquireRetryAttempts(5);
        config.setAcquireRetryDelayInMs(300);
        config.setDefaultAutoCommit(false);
        config.setPoolAvailabilityThreshold(5);
        config.setDetectUnclosedStatements(true);
        config.setCloseOpenStatements(true);
        config.setConnectionTestStatement("SELECT 1;");
        config.setIdleConnectionTestPeriodInMinutes(2);
        config.setIdleMaxAgeInMinutes(4);

        try {
            pool = new BoneCP(config);
        } catch (SQLException e) {
            logger.warn("can't init BoneCP Connection Pool", e);
            throw e;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        logger.debug("return connection from BoneCPConnectionPool");
        return pool.getConnection();
    }

    @Override
    public void shutDown() {
        logger.debug("shut down BoneCPConnectionPool");
        if (pool != null) {
            pool.shutdown();
        }
    }
}
