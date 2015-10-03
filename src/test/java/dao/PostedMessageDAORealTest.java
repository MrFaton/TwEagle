package dao;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.dao.impl.PostedMessageDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.table.PostedMessage;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 03.10.2015
 */
public class PostedMessageDAORealTest {
    private static TransactionManager transactionManager;
    private static PostedMessageDAO postedMessageDAO;

    @BeforeClass
    public static void generalSetUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
        postedMessageDAO = new PostedMessageDAOReal(transactionManager);
    }
    @AfterClass
    public static void generalTearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.posted_messages WHERE owner LIKE 'PostedMessageDAOReal%';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
        if (transactionManager != null) transactionManager.shutDown();
    }

    @Test
    public void savePostedMessageTest() throws Exception {
        final String pushingOwner = "PostedMessageDAOReal1";
        final PostedMessage postedMessage = new PostedMessage();
        postedMessage.setMessage("test");
        postedMessage.setMessageId(123);
        postedMessage.setTweet(true);
        postedMessage.setOwner(pushingOwner);
        postedMessage.setPostedDate(new Date());

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                postedMessageDAO.savePostedMessage(postedMessage);
            }
        });

        final String SQL = "" +
                "SELECT * FROM tweagle.posted_messages WHERE owner = 'PostedMessageDAOReal1';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    String takenOwner = resultSet.getString("owner");
                    Assert.assertEquals(pushingOwner, takenOwner);
                } else {
                    Assert.fail(" result set must have next");
                }
            }
        });
    }
}
