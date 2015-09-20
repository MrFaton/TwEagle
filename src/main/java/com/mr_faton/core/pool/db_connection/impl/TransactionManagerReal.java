package com.mr_faton.core.pool.db_connection.impl;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mr_faton.core.pool.db_connection.TransactionManagerBase;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Description
 *
 * @author Mr_Faton
 * @since 11.09.2015
 * @version 1.0
 */

public class TransactionManagerReal extends TransactionManagerBase {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal");
    private static final ThreadLocal<Connection> CONNECTION_STORAGE = new ThreadLocal<>();
    private static final String JDBC_URL = SettingsHolder.getSetupByKey("JDBC_URL");
    private static final String JDBC_USER = SettingsHolder.getSetupByKey("JDBC_USER");
    private static final String JDBC_USER_PASSWORD = SettingsHolder.getSetupByKey("JDBC_USER_PASSWORD");
    private final BoneCP connectionPool;

    public TransactionManagerReal() throws ClassNotFoundException, SQLException {
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
        config.setMaxConnectionsPerPartition(4);
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
            connectionPool = new BoneCP(config);
        } catch (SQLException e) {
            logger.warn("can't init BoneCP Connection Pool", e);
            throw e;
        }
    }


    @Override
    public void doInTransaction(Command command) {
        logger.debug("start transaction");
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            CONNECTION_STORAGE.set(connection);

            command.doCommands();

            connection.commit();
            logger.debug("commit connection");
        } catch (Exception ex) {
            logger.warn("exception", ex);
            if (connection != null) {
                try {
                    connection.rollback();
                    logger.debug("roll back connection");
                } catch (SQLException e) {
                    logger.warn("exception while rollback connection", e);
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warn("exception while close connection", e);
                }
            }
            CONNECTION_STORAGE.remove();
        }
    }


    @Override
    public Connection getConnection() {
        logger.debug("get connection from ThreadLocal");
        return CONNECTION_STORAGE.get();
    }

    @Override
    public void shutDown() {
        logger.info("shutdown connection pool");
        connectionPool.shutdown();
    }
}
