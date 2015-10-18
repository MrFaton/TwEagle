package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.TweetUser;
import com.mr_faton.core.util.Command;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class TweetUserDAORealTest {
    private static final String BASE_NAME = "TweetUserDAOReal";
    private static TransactionManager transactionManager;
    private static TweetUserDAO tweetUserDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        tweetUserDAO = new TweetUserDAOReal(transactionManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.tweet_users WHERE name LIKE 'TweetUserDAOReal%';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }


    @Test
    public void getUserForTweet() throws Exception {
        final TweetUser tweetUser = createTweetUser();

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.save(tweetUser);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.getUserForTweet();
            }
        });
    }

    @Test
    public void getUserList() throws Exception {
        final List<TweetUser> tweetUserList = new ArrayList<>(3);
        tweetUserList.add(createTweetUser());
        tweetUserList.add(createTweetUser());

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.save(tweetUserList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                List<TweetUser> receivedList = tweetUserDAO.getUserList();
                Assert.assertTrue(receivedList.size() >= 2);
            }
        });
    }



    @Test
    public void saveAndUpdate() throws Exception {
        final TweetUser tweetUser = createTweetUser();

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.save(tweetUser);
            }
        });

        tweetUser.setCurTweets(7);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.update(tweetUser);
            }
        });
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final TweetUser tweetUser1 = createTweetUser();
        final TweetUser tweetUser2 = createTweetUser();

        final List<TweetUser> tweetUserList = new ArrayList<>(3);
        tweetUserList.add(tweetUser1);
        tweetUserList.add(tweetUser2);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.save(tweetUserList);
            }
        });

        tweetUser1.setMaxTweets(20);
        tweetUser2.setMaxTweets(25);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                tweetUserDAO.update(tweetUserList);
            }
        });
    }


    private TweetUser createTweetUser() {
        TweetUser tweetUser = new TweetUser();

        tweetUser.setName(BASE_NAME + Counter.getNextNumber());
        tweetUser.setTweet(true);
        tweetUser.setCurTweets(5);
        tweetUser.setMaxTweets(10);
        tweetUser.setNextTweet(System.currentTimeMillis() + 30_000);
        Calendar calendar = Calendar.getInstance();
        tweetUser.setLastUpdateDay(calendar.get(Calendar.DAY_OF_MONTH));

        return tweetUser;
    }
}
