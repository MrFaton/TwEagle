package dao;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.dao.impl.UserDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

    @Test
    public void updateUserTest() throws Exception {
        final String pushingPassword = "567";
        final int pushingMessages = 7777;
        final User user = new User();
        user.setName("UserDAOReal1");
        user.setPassword(pushingPassword);
        user.setEmail("test@email.com");
        user.setMale(false);
        user.setCreationDate(new Date(System.currentTimeMillis()));
        user.setMessages(pushingMessages);
        user.setFollowing(123);
        user.setFollowers(456);
        user.setConsumerKey("111");
        user.setConsumerSecret("222");
        user.setAccessToken("333");
        user.setAccessTokenSecret("444");

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.update(user);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                final String SQL = "" +
                        "SELECT * FROM tweagle.users WHERE name = 'UserDAOReal1';";
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    String takenPassword = resultSet.getString("password");
                    Assert.assertEquals(pushingPassword, takenPassword);

                    int takenMessages = resultSet.getInt("messages");
                    Assert.assertEquals(pushingMessages, takenMessages);
                } else {
                    Assert.fail("result set must be not null");
                }
            }
        });
    }

    @Test
    public void updateUserListTest() throws Exception {
        final String pushingPassword = "9999";
        final int pushingMessages = 8888;
        final User user = new User();
        user.setName("UserDAOReal2");
        user.setPassword(pushingPassword);
        user.setEmail("test@email.com");
        user.setMale(false);
        user.setCreationDate(new Date(System.currentTimeMillis()));
        user.setMessages(pushingMessages);
        user.setFollowing(123);
        user.setFollowers(456);
        user.setConsumerKey("111");
        user.setConsumerSecret("222");
        user.setAccessToken("333");
        user.setAccessTokenSecret("444");

        final List<User> userList = new ArrayList<>();
        userList.add(user);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.update(userList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                final String SQL = "" +
                        "SELECT * FROM tweagle.users WHERE name = 'UserDAOReal2';";
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    String takenPassword = resultSet.getString("password");
                    Assert.assertEquals(pushingPassword, takenPassword);

                    int takenMessages = resultSet.getInt("messages");
                    Assert.assertEquals(pushingMessages, takenMessages);
                } else {
                    Assert.fail("result set must be not null");
                }
            }
        });
    }

    @Test
    public void addUserTest() throws Exception {
        String pushingName = "UserDAOReal777";
        final int pusingMessages = 1111111111;
        final User user = new User();
        user.setName(pushingName);
        user.setPassword("123456789");
        user.setEmail("test@email.com");
        user.setMale(false);
        user.setCreationDate(new Date(System.currentTimeMillis()));
        user.setMessages(pusingMessages);
        user.setFollowing(123);
        user.setFollowers(456);
        user.setConsumerKey("111");
        user.setConsumerSecret("222");
        user.setAccessToken("333");
        user.setAccessTokenSecret("444");

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.addUser(user);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                final String SQL = "" +
                        "SELECT * FROM tweagle.users WHERE name = 'UserDAOReal777';";
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    int takenMessages = resultSet.getInt("messages");
                    Assert.assertEquals(pusingMessages, takenMessages);
                } else {
                    Assert.fail("result set must be not null");
                }
            }
        });
    }
}
