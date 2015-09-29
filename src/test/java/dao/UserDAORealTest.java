package dao;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.dao.impl.UserDAOReal;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.*;
import org.junit.internal.ExactComparisonCriteria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.09.2015
 */
public class UserDAORealTest {
    private static TransactionManager transactionManager = null;
    private static UserDAO userDAO;

    @BeforeClass()
    public static void generalSetUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
        userDAO = new UserDAOReal(transactionManager);

        final String SQL1 = "" +
                "INSERT INTO tweagle.users (name, password, email, male, creation_date) VALUES " +
                "('UserDAOReal1', '123', 'some_mail@mail.ru', 1, '2015-09-29');";
        final String SQL2 = "" +
                "INSERT INTO tweagle.users (name, password, email, male, creation_date) VALUES " +
                "('UserDAOReal2', '123', 'some_mail@mail.ru', 1, '2015-09-29');";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                statement.addBatch(SQL1);
                statement.addBatch(SQL2);
                statement.executeBatch();
            }
        });
    }

    @AfterClass
    public static void generalTearDown() throws Exception{
        final String SQL = "" +
                "DELETE FROM tweagle.users WHERE name LIKE 'UserDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });

        if (transactionManager != null)transactionManager.shutDown();
    }


    @Test
    public void getUserByNameTest() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                    userDAO.getUserByName("UserDAOReal1");
            }
        });
    }

    @Test
    public void getUserListTest() throws Exception{
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                List<User> userList = userDAO.getUserList();
                Assert.assertNotNull("user list null", userList);
                int listSize = userList.size();
                Assert.assertTrue("list size less then expected", listSize >= 2);
            }
        });
    }
}
