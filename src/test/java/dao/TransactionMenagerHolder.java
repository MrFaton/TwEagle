package dao;

import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.util.SettingsHolder;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class TransactionMenagerHolder {
    private static TransactionManager transactionManager = null;

    static synchronized TransactionManager getTransactionManager() throws Exception {
        if (transactionManager == null) {
            SettingsHolder.loadSettings();
            transactionManager = new TransactionManagerReal();
        }
        return transactionManager;
    }
}
