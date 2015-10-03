package dao;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.dao.impl.TweetUserDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
import com.mr_faton.core.table.TweetUser;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 03.10.2015
 */
public class TweetUserDAORealTest {
    private static TransactionManager transactionManager;
    private static TweetUserDAO tweetUserDAO;

    @BeforeClass
    public static void generalSetUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = new TransactionManagerReal();
        tweetUserDAO = new TweetUserDAOReal(transactionManager);

        final String SQL1 = "" +
                "INSERT INTO tweagle.tweet_users (name, is_tweet, cur_tweet, max_tweet, next_tweet, last_upd) " +
                "VALUES ('TweetUserDAOReal1', 1, 10, 10, '2015-10-03 09:15:20', 10);";
        final String SQL2 = "" +
                "INSERT INTO tweagle.tweet_users (name, is_tweet, cur_tweet, max_tweet, next_tweet, last_upd) " +
                "VALUES ('TweetUserDAOReal2', 1, 10, 10, '2015-10-03 05:20:36', 10);";
        final String SQL3 = "" +
                "INSERT INTO tweagle.tweet_users (name, is_tweet, cur_tweet, max_tweet, next_tweet, last_upd) " +
                "VALUES ('TweetUserDAOReal3', 1, 5, 10, '2015-10-03 05:20:36', 10);";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                statement.addBatch(SQL1);
                statement.addBatch(SQL2);
                statement.addBatch(SQL3);
                statement.executeBatch();
            }
        });
    }

    @AfterClass
    public static void generalTearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.tweet_users WHERE name LIKE 'TweetUserDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });

        if (transactionManager != null) transactionManager.shutDown();
    }

    @Test
    public void getUserForTweetTest() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                TweetUser tweetUser = tweetUserDAO.getUserForTweet();
                Assert.assertEquals("TweetUserDAOReal3", tweetUser.getName());
            }
        });

    }

    @Test
    public void getUserListTest() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                List<TweetUser> tweetUserList = tweetUserDAO.getUserList();
                Assert.assertTrue(tweetUserList.size() >= 2);
            }
        });
    }

    @Test
    public void updateUserTest() throws Exception {
        final int pushingMaxTweets = 1000;
        final TweetUser tweetUser = new TweetUser();
        tweetUser.setName("TweetUserDAOReal1");
        tweetUser.setTweet(true);
        tweetUser.setCurTweets(1);
        tweetUser.setMaxTweets(pushingMaxTweets);
        tweetUser.setNextTweet(System.currentTimeMillis());
        tweetUser.setLastUpdateDay(9);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.updateUser(tweetUser);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                final String SQL = "" +
                        "SELECT * FROM tweagle.tweet_users WHERE name = 'TweetUserDAOReal1';";
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    int takenMaxTweets = resultSet.getInt("max_tweet");
                    Assert.assertEquals(pushingMaxTweets, takenMaxTweets);
                } else {
                    Assert.fail("result set must be not null");
                }
            }
        });
    }

    @Test
    public void updateUserListTest() throws Exception {
        final int pushingMaxTweets = 1111;
        final TweetUser tweetUser = new TweetUser();
        tweetUser.setName("TweetUserDAOReal2");
        tweetUser.setTweet(true);
        tweetUser.setCurTweets(1);
        tweetUser.setMaxTweets(pushingMaxTweets);
        tweetUser.setNextTweet(System.currentTimeMillis());
        tweetUser.setLastUpdateDay(9);

        final List<TweetUser> tweetUserList = new ArrayList<>();
        tweetUserList.add(tweetUser);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.updateUserList(tweetUserList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                final String SQL = "" +
                        "SELECT * FROM tweagle.tweet_users WHERE name = 'TweetUserDAOReal2';";
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    int takenMaxTweets = resultSet.getInt("max_tweet");
                    Assert.assertEquals(pushingMaxTweets, takenMaxTweets);
                } else {
                    Assert.fail("result set must have next");
                }
            }
        });
    }

    @Test
    public void addUserTest() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.addUser("TweetUserDAOReal777");
            }
        });

        final String SQL = "" +
                "SELECT * FROM tweagle.tweet_users WHERE name = 'TweetUserDAOReal777';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);

                if (resultSet.next()) {
                    String takenUserName = resultSet.getString("name");
                    Assert.assertEquals("TweetUserDAOReal777", takenUserName);
                } else {
                    Assert.fail("result set must have next");
                }
            }
        });
    }

}
