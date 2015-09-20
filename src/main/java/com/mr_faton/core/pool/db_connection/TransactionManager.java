package com.mr_faton.core.pool.db_connection;

import com.mr_faton.core.util.Command;

import javax.sql.DataSource;

public interface TransactionManager extends DataSource{
    void doInTransaction(Command command);

    void shutDown();
}
