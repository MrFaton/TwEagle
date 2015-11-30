//package com.mr_faton.core.api.impl;
//
//import com.mr_faton.core.api.TwitterAPI;
//import com.mr_faton.core.dao.UserDAO;
//import com.mr_faton.core.dao.impl.UserDAOReal;
////import com.mr_faton.core.pool.db_connection.TransactionManager;
////import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
//import com.mr_faton.core.util.Command;
//import com.mr_faton.core.util.SettingsHolder;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
///**
// * Description
// *
// * @author Mr_Faton
// * @since 05.10.2015
// * @version 1.0
// */
//public class TwitterAPIRealTest {
////    private static TransactionManager transactionManager;
//    private static UserDAO userDAO;
//    private static TwitterAPI twitterAPI;
//    private static final String TEST_USER = "Mr_Faton";
//
////    @BeforeClass
////    public static void generalSetUp() throws SQLException, ClassNotFoundException, IOException {
////        SettingsHolder.loadSettings();
////        transactionManager = new TransactionManagerReal();
////        userDAO = new UserDAOReal(transactionManager);
////        twitterAPI = new TwitterAPIReal(userDAO);
////    }
//
////    @AfterClass
////    public static void generalTearDown() {
////        if (transactionManager != null) transactionManager.shutDown();
////    }
//
//
////    @Test
////    public void postTweet() throws Exception{
////        transactionManager.doInTransaction(new Command() {
////            @Override
////            public void doCommands() throws Exception {
////                twitterAPI.postTweet(TEST_USER, "Всем приветики!");
////            }
////        });
////    }
//
////    @Test
////    public void deleteLastTweet() throws Exception {
////        transactionManager.doInTransaction(new Command() {
////            @Override
////            public void doCommands() throws Exception {
////                twitterAPI.deleteLastTweet(TEST_USER);
////            }
////        });
////    }
//}
