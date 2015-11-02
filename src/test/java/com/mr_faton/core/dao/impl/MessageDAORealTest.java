package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.Message;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class MessageDAORealTest {
//    private static final String BASE_NAME = "MessageDAOReal";
//    private static TransactionManager transactionManager;
//    private static MessageDAO messageDAO;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        transactionManager = TransactionMenagerHolder.getTransactionManager();
//        messageDAO = new MessageDAOReal(transactionManager);
//    }
//
//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String SQL = "" +
//                "DELETE FROM tweagle.messages WHERE owner LIKE 'MessageDAOReal%';";
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                transactionManager.getConnection().createStatement().executeUpdate(SQL);
//            }
//        });
//    }
//
//
//    @Test
//    public void getTweetFirstTry() throws Exception {
//        final Message message = createMessage();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -3);
//        message.setPostedDate(new Date(calendar.getTimeInMillis()));
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(message);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.getTweetFirstTry(message.isOwnerMale());
//            }
//        });
//    }
//    @Test
//    public void getTweetSecondTry() throws Exception {
//        final Message message = createMessage();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -3);
//        calendar.add(Calendar.DAY_OF_MONTH, -3);
//
//        message.setPostedDate(new Date(calendar.getTimeInMillis()));
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(message);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.getTweetSecondTry(message.isOwnerMale());
//            }
//        });
//    }
//    @Test
//    public void getTweetThirdTry() throws Exception {
//        final Message message = createMessage();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -5);
//        message.setPostedDate(new Date(calendar.getTimeInMillis()));
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(message);
//            }
//        });
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.getTweetThirdTry(message.isOwnerMale());
//            }
//        });
//    }
//    @Test
//    public void getAnyTweet() throws Exception {
//        final Message message = createMessage();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, -6);
//        message.setPostedDate(new Date(calendar.getTimeInMillis()));
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(message);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.getAnyTweet(message.isOwnerMale());
//            }
//        });
//    }
//
//    @Test
//    public void getUnSynonymizedMessages() throws Exception {
//        final List<Message> messageList = new ArrayList<>(3);
//        final Message message1 = createMessage();
//        final Message message2 = createMessage();
//
//        message1.setSynonymized(false);
//        message2.setSynonymized(false);
//
//        messageList.add(message1);
//        messageList.add(message2);
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(messageList);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                int expectedSize = 2;
//                List<Message> takenMessages = messageDAO.getUnSynonymizedMessages(expectedSize);
//                Assert.assertEquals(expectedSize, takenMessages.size());
//            }
//        });
//    }
//
//
//    @Test
//    public void saveAndUpdate() throws Exception {
//        final Message message = createMessage();
//        final String SQL = "" +
//                "SELECT id FROM tweagle.messages WHERE owner = '" + message.getOwner() + "';";
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(message);
//            }
//        });
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                Connection connection = transactionManager.getConnection();
//                try (Statement statement = connection.createStatement();
//                     ResultSet resultSet = statement.executeQuery(SQL)) {
//                    if (resultSet.next()) {
//                        message.setId(resultSet.getInt("id"));
//                    } else {
//                        Assert.fail();
//                    }
//                }
//            }
//        });
//
//        message.setPosted(true);
//        message.setSynonymized(false);
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.update(message);
//            }
//        });
//    }
//
//    @Test
//    public void saveAndUpdateList() throws Exception {
//        final Message message1 = createMessage();
//        final Message message2 = createMessage();
//
//        final String SQL1 = "" +
//                "SELECT id FROM tweagle.messages WHERE owner = '" + message1.getOwner() + "';";
//        final String SQL2 = "" +
//                "SELECT id FROM tweagle.messages WHERE owner = '" + message2.getOwner() + "';";
//
//        final List<Message> messageList = new ArrayList<>(3);
//        messageList.add(message1);
//        messageList.add(message2);
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.save(messageList);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                Connection connection = transactionManager.getConnection();
//                try(Statement statement = connection.createStatement()) {
//                    ResultSet resultSet = statement.executeQuery(SQL1);
//                    if (resultSet.next()) {
//                        message1.setId(resultSet.getInt("id"));
//                    } else {
//                        Assert.fail();
//                    }
//                    resultSet = statement.executeQuery(SQL2);
//                    if (resultSet.next()) {
//                        message2.setId(resultSet.getInt("id"));
//                    } else {
//                        Assert.fail();
//                    }
//                }
//            }
//        });
//
//        message1.setPosted(true);
//        message1.setSynonymized(false);
//
//        message2.setPosted(true);
//        message2.setSynonymized(false);
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                messageDAO.update(messageList);
//            }
//        });
//    }
//
//
//    private Message createMessage() {
//        Message message = new Message();
//
//        message.setMessage("Test");
//
//        message.setOwner(BASE_NAME + Counter.getNextNumber());
//        message.setOwnerMale(true);
//
//        message.setRecipient(null);
//        message.setRecipientMale(false);
//
//        message.setPostedDate(new Date());
//
//        message.setSynonymized(true);
//        message.setPosted(false);
//
//        return message;
//    }
}
