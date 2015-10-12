package dao;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.impl.MessageDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 05.10.2015
 */
public class MessageDAORealTest {
    private static TransactionManager transactionManager;
    private static MessageDAO messageDAO;

    @BeforeClass
    public static void generalSetUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
        messageDAO = new MessageDAOReal(transactionManager);

        final String getTweetSql = "" +
                "INSERT INTO tweagle.messages (message, tweet, owner, owner_male, posted_date, synonymized) VALUES " +
                "(?, ?, ?, ?, ?, ?);";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Calendar calendar;
                Connection connection = transactionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getTweetSql);

                // For First Try
                preparedStatement.setString(1, "Test for get tweet first try");
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "MessageDAOReal1");
                preparedStatement.setBoolean(4, true);
                calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -3);
                preparedStatement.setDate(5, new Date(calendar.getTimeInMillis()));
                preparedStatement.setBoolean(6, true);
                preparedStatement.addBatch();

                // For second try
                preparedStatement.setString(1, "Test for get tweet second try");
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "MessageDAOReal2");
                preparedStatement.setBoolean(4, true);
                calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -2);
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                preparedStatement.setDate(5, new Date(calendar.getTimeInMillis()));
                preparedStatement.setBoolean(6, true);
                preparedStatement.addBatch();

                // For update posted message
                preparedStatement.setString(1, "Test for update posted message");
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "MessageDAOReal3");
                preparedStatement.setBoolean(4, true);
                preparedStatement.setString(5, "2000-10-10");
                preparedStatement.setBoolean(6, true);
                preparedStatement.addBatch();

                preparedStatement.executeBatch();
            }
        });
    }

    @AfterClass
    public static void generalTearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.messages WHERE owner LIKE 'MessageDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });

        if (transactionManager != null) transactionManager.shutDown();
    }


    @Test
    public void getTweetFirstTry() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.getTweetFirstTry(true);
            }
        });
    }
    @Test
    public void getTweetSecondTry() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.getTweetSecondTry(true);
            }
        });
    }
    @Test
    public void getTweetThirdTry() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.getTweetThirdTry(true);
            }
        });
    }
    @Test
    public void getAnyTweet() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.getAnyTweet(true);
            }
        });
    }




    @Test
    public void updatePostedMessage() throws Exception {
        final int[] id = new int[1];
        final String SQL1 = "" +
                "SELECT id FROM tweagle.messages WHERE owner = 'MessageDAOReal3';";
        final String SQL2 = "" +
                "SELECT posted FROM tweagle.messages WHERE owner = 'MessageDAOReal3';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL1);
                if (resultSet.next()) {
                    id[0] = resultSet.getInt("id");
                } else {
                    Assert.fail("result set must have next");
                }
            }
        });

        final Message message = new Message();
        message.setId(id[0]);
        message.setOwner("MessageDAOReal3");
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.updatePostedMessage(message);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL2);

                if (resultSet.next()) {
                    Assert.assertTrue(resultSet.getBoolean("posted"));
                } else {
                    Assert.fail("result set must have next");
                }
            }
        });
    }

    @Test
    public void saveMessageListTest() throws Exception {
        final String SQL = "" +
                "SELECT owner FROM tweagle.messages WHERE owner = 'MessageDAOReal44';";

        Message message = new Message();
        message.setMessage("testing");
        message.setTweet(true);
        message.setOwner("MessageDAOReal44");
        message.setOwnerMale(true);
        message.setPostedDate(new java.util.Date());

        final List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                messageDAO.save(messageList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    String takenOwnerName = resultSet.getString("owner");
                    Assert.assertEquals("must equal", "MessageDAOReal44", takenOwnerName);
                } else {
                    Assert.fail("resultSet must have next");
                }
            }
        });
    }
}
