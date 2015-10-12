package dao;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.impl.DonorUserDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 09.10.2015
 */
public class DonorUserDAORealTest {
    private static TransactionManager transactionManager;
    private static DonorUserDAO donorUserDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
        donorUserDAO = new DonorUserDAOReal(transactionManager);
        final String SQL = "" +
                "INSERT INTO tweagle.donor_users (donor_name, is_male) VALUES ('DonorUserDAORealTest1', 1);";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE donor_name LIKE 'DonorUserDAORealTest%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
        transactionManager.shutDown();
    }


    @Test
    public void getDonorForMessage() throws Exception{
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.getDonorForMessage();
            }
        });
    }
}
