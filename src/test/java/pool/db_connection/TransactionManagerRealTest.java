package pool.db_connection;

import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 07.10.2015
 */
public class TransactionManagerRealTest {
    private static TransactionManager transactionManager;

    @BeforeClass
    public static void generalSetUp() throws Exception{
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
    }

    @AfterClass
    public static void generalTearDown() {
        if (transactionManager != null) transactionManager.shutDown();
    }

    @Test
    public void getConnection() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Assert.assertNotNull("connection must be not null", connection);
            }
        });
    }
}
