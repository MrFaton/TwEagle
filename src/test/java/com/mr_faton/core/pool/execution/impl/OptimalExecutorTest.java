//package com.mr_faton.core.pool.execution.impl;
//
//import com.mr_faton.core.execution_pool.ExecutionPool;
//import com.mr_faton.core.execution_pool.impl.OptimalExecutor;
////import com.mr_faton.core.pool.db_connection.TransactionManager;
////import com.mr_faton.core.pool.db_connection.impl.TransactionManagerReal;
//import com.mr_faton.core.task.Task;
//import com.mr_faton.core.util.SettingsHolder;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.sql.SQLException;
//
///**
// * Description
// *
// * @author Mr_Faton
// * @version 1.0
// * @since 07.10.2015
// */
//public class OptimalExecutorTest {
//    private static Task task;
//    private static ExecutionPool executionPool;
////    private static TransactionManager transactionManager;
//
//    @BeforeClass
//    public static void generalSetUp() throws Exception{
//        SettingsHolder.loadSettings();
////        transactionManager = new TransactionManagerReal();
////        executionPool = new OptimalExecutor(transactionManager);
//
//        task = new Task() {
//            @Override
//            public boolean getStatus() {
//                return true;
//            }
//
//            @Override
//            public void setStatus(boolean status) {
//                /*NOP*/
//            }
//
//            @Override
//            public long getTime() {
//                return 0;
//            }
//
//            @Override
//            public void setNextTime() {
//                /*NOP*/
//            }
//
//            @Override
//            public void update() throws SQLException {
//                /*NOP*/
//            }
//
//            @Override
//            public void save() throws SQLException {
//                /*NOP*/
//            }
//
//            @Override
//            public void execute() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    /*NOP*/
//                }
//            }
//
//            @Override
//            public void setDailyParams() throws SQLException {
//                /*NOP*/
//            }
//        };
//    }
//
////    @AfterClass
////    public static void generalTearDown() {
////        if (transactionManager != null) transactionManager.shutDown();
////        if (executionPool != null) executionPool.shutDown();
////    }
//
//    @Test
//    public void execute() {
//        for (int i = 0; i < 5; i++) {
//            executionPool.execute(task);
//        }
//
//        OptimalExecutor optimalExecutor = (OptimalExecutor) executionPool;
////        int maxThreads = optimalExecutor.getLargestPoolSize();
////        System.out.println(maxThreads);
////        Assert.assertTrue(maxThreads > 1);
//
//
//    }
//}
