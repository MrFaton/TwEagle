package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.PostedMessage;
import com.mr_faton.core.util.Command;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class PostedMessageDAORealTest {
    private static final String BASE_NAME = "PostedMessageDAOReal";
    private static TransactionManager transactionManager;
    private static PostedMessageDAO postedMessageDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        postedMessageDAO = new PostedMessageDAOReal(transactionManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.posted_messages WHERE owner LIKE 'PostedMessageDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }


    @Test
    public void saveAndUpdate() throws Exception {
        final PostedMessage postedMessage = createPostedMessage();
        final String SQL = "" +
                "SELECT id FROM tweagle.posted_messages WHERE owner = '" + postedMessage.getOwner() + "';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                postedMessageDAO.save(postedMessage);
            }
        });

        postedMessage.setTweet(false);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(SQL)) {
                    if (resultSet.next()) {
                        postedMessage.setId(resultSet.getInt("id"));
                    } else {
                        Assert.fail("result set must have next");
                    }
                }
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                postedMessageDAO.update(postedMessage);
            }
        });
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final PostedMessage postedMessage1 = createPostedMessage();
        final PostedMessage postedMessage2 = createPostedMessage();

        final String SQL1 = "" +
                "SELECT id FROM tweagle.posted_messages WHERE owner = '" + postedMessage1.getOwner() + "';";
        final String SQL2 = "" +
                "SELECT id FROM tweagle.posted_messages WHERE owner = '" + postedMessage2.getOwner() + "';";

        final List<PostedMessage> postedMessageList = new ArrayList<>(3);
        postedMessageList.add(postedMessage1);
        postedMessageList.add(postedMessage2);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                postedMessageDAO.save(postedMessageList);
            }
        });

        postedMessage1.setTweet(false);
        postedMessage1.setMessage("To-to1");
        postedMessage2.setTweet(false);
        postedMessage2.setMessage("To-to2");

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(SQL1);
                    if (resultSet.next()) {
                        postedMessage1.setId(resultSet.getInt("id"));
                    } else {
                        Assert.fail();
                    }
                    resultSet = statement.executeQuery(SQL2);
                    if (resultSet.next()) {
                        postedMessage2.setId(resultSet.getInt("id"));
                    } else {
                        Assert.fail();
                    }
                }
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                postedMessageDAO.update(postedMessageList);
            }
        });
    }


    private PostedMessage createPostedMessage() {
        PostedMessage postedMessage = new PostedMessage();

        postedMessage.setMessage("Test posted message");
        postedMessage.setMessageId(new Random().nextInt(10_000));
        postedMessage.setTweet(true);
        postedMessage.setOwner(BASE_NAME + Counter.getNextNumber());
        postedMessage.setRecipient(null);
        postedMessage.setPostedDate(new Date());

        return postedMessage;
    }
}
