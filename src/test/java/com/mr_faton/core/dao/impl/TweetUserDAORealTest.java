//package com.mr_faton.core.dao.impl;
//
//import com.mr_faton.core.context.AppContext;
//import com.mr_faton.core.dao.TweetUserDAO;
//import com.mr_faton.core.dao.UserDAO;
//import com.mr_faton.core.exception.NoSuchEntityException;
//import com.mr_faton.core.table.TweetUser;
//import com.mr_faton.core.table.User;
//import org.junit.AfterClass;
//import org.junit.Test;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import util.Counter;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
///**
// * Description
// *
// * @author root
// * @version 1.0
// * @since 14.10.2015
// */
//public class TweetUserDAORealTest {
//    private static final String BASE_NAME = "TweetUserDAOReal";
//    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
//    private static final UserDAO USER_DAO = (UserDAO) AppContext.getBeanByName("userDAO");
//    private static final TweetUserDAO TWEET_USER_DAO = (TweetUserDAO) AppContext.getBeanByName("tweetUserDAO");
//
//
//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String SQL_DELETE_FROM_TWEET_USERS = "" +
//                "DELETE FROM tweagle.tweet_users WHERE tu_name LIKE 'TweetUserDAOReal%';";
//        final String SQL_DELETE_FROM_USERS = "" +
//                "DELETE FROM tweagle.users WHERE u_name LIKE 'TweetUserDAOReal%';";
//        JDBC_TEMPLATE.update(SQL_DELETE_FROM_TWEET_USERS);
//        JDBC_TEMPLATE.update(SQL_DELETE_FROM_USERS);
//    }
//
//
//    @Test
//    public void getUserForTweet() throws Exception {
//        User user = createDefaultUser();
//        TweetUser original = createDefaultTweetUser();
//        original.setName(user.getName());
//        original.setTweet(true);
//        original.setCurTweets(1);
//        original.setMaxTweets(5);
//        original.setNextTweet(new Date(System.currentTimeMillis() + 10_000));
//
//        USER_DAO.save(user);
//        TWEET_USER_DAO.save(original);
//
//        TweetUser extracted = TWEET_USER_DAO.getUserForTweet();
//
//        assertTrue(extracted.isTweet());
//        assertTrue(extracted.getCurTweets() < extracted.getMaxTweets());
//        assertNotNull(extracted.getNextTweet());
//    }
//
//    @Test
//    public void getUserList() throws Exception {
//        User user1 = createDefaultUser();
//        User user2 = createDefaultUser();
//
//        TweetUser original1 = createDefaultTweetUser();
//        original1.setName(user1.getName());
//
//        TweetUser original2 = createDefaultTweetUser();
//        original2.setName(user2.getName());
//
//        List<User> userList = Arrays.asList(user1, user2);
//        List<TweetUser> originalList = Arrays.asList(original1, original2);
//
//        USER_DAO.save(userList);
//        TWEET_USER_DAO.save(originalList);
//
//        List<TweetUser> tweetUserList = TWEET_USER_DAO.getUserList();
//        assertTrue(tweetUserList.size() >= 2);
//    }
//
//
//
//    @Test
//    public void saveAndUpdate() throws Exception {
//        User user = createDefaultUser();
//        TweetUser original = createDefaultTweetUser();
//        original.setName(user.getName());
//
//        //Test save
//        USER_DAO.save(user);
//        TWEET_USER_DAO.save(original);
//
//        TweetUser extracted = getTweetUserByUniqueName(original.getName());
//
//        equalsTweetUsers(original, extracted);
//
//        //Test update
//        original.setTweet(!original.isTweet());
//        original.setCurTweets(original.getCurTweets() + 1);
//        original.setMaxTweets(original.getMaxTweets() + 1);
//        original.setNextTweet(new Date(System.currentTimeMillis() + 15_000));
//        original.setLastUpdateDay(original.getLastUpdateDay() + 1);
//
//        TWEET_USER_DAO.update(original);
//
//        extracted = getTweetUserByUniqueName(original.getName());
//
//        equalsTweetUsers(original, extracted);
//    }
//
//    @Test
//    public void saveAndUpdateList() throws Exception {
//        User user1 = createDefaultUser();
//        User user2 = createDefaultUser();
//
//        TweetUser original1 = createDefaultTweetUser();
//        original1.setName(user1.getName());
//
//        TweetUser original2 = createDefaultTweetUser();
//        original2.setName(user2.getName());
//
//        List<User> userList = Arrays.asList(user1, user2);
//        List<TweetUser> originalList = Arrays.asList(original1, original2);
//
//        //Test save
//        USER_DAO.save(userList);
//        TWEET_USER_DAO.save(originalList);
//
//        TweetUser extracted1 = getTweetUserByUniqueName(original1.getName());
//        TweetUser extracted2 = getTweetUserByUniqueName(original2.getName());
//
//        equalsTweetUsers(original1, extracted1);
//        equalsTweetUsers(original2, extracted2);
//
//        //Test update
//        original1.setTweet(!original1.isTweet());
//        original1.setCurTweets(original1.getCurTweets() + 1);
//        original1.setMaxTweets(original1.getMaxTweets() + 1);
//        original1.setNextTweet(new Date(System.currentTimeMillis() + 15_000));
//        original1.setLastUpdateDay(original1.getLastUpdateDay() + 1);
//
//        original2.setTweet(!original2.isTweet());
//        original2.setCurTweets(original2.getCurTweets() + 2);
//        original2.setMaxTweets(original2.getMaxTweets() + 2);
//        original2.setNextTweet(new Date(System.currentTimeMillis() + 25_000));
//        original2.setLastUpdateDay(original2.getLastUpdateDay() + 1);
//
//        TWEET_USER_DAO.update(originalList);
//
//        extracted1 = getTweetUserByUniqueName(original1.getName());
//        extracted2 = getTweetUserByUniqueName(original2.getName());
//
//        equalsTweetUsers(original1, extracted1);
//        equalsTweetUsers(original2, extracted2);
//    }
//
//
//    private User createDefaultUser() {
//        User user = new User();
//
//        user.setName(BASE_NAME + Counter.getNextNumber());
//        user.setPassword("secret");
//        user.setEmail("testmail@test.com");
//        user.setMale(true);
//        user.setCreationDate(new Date());
//        user.setMessages(100);
//        user.setFollowing(200);
//        user.setFollowers(300);
//        user.setConsumerKey("consumer key");
//        user.setConsumerSecret("consumer secret");
//        user.setAccessToken("access token");
//        user.setAccessTokenSecret("access token secret");
//
//        return user;
//    }
//
//    private TweetUser createDefaultTweetUser() {
//        TweetUser tweetUser = new TweetUser();
//
//        tweetUser.setTweet(true);
//        tweetUser.setCurTweets(1);
//        tweetUser.setMaxTweets(5);
//        tweetUser.setNextTweet(new Date(System.currentTimeMillis() + 10_000));
//        tweetUser.setLastUpdateDay(1);
//
//        return tweetUser;
//    }
//
//    private TweetUser getTweetUserByUniqueName(String name) throws NoSuchEntityException {
//        final String SQL = "" +
//                "SELECT * FROM tweagle.tweet_users WHERE tu_name = '" + name + "';";
////        try {
////            return JDBC_TEMPLATE.queryForObject(SQL, new TwitterUserRowMapper());
////        } catch (EmptyResultDataAccessException emptyData) {
////            throw new NoSuchEntityException();
////        }
//        return null;
//    }
//
//    private void equalsTweetUsers(TweetUser original, TweetUser extracted) {
//        assertEquals(original.getName(), extracted.getName());
//        assertEquals(original.isTweet(), extracted.isTweet());
//        assertTrue(original.getCurTweets() == extracted.getCurTweets());
//        assertTrue(original.getMaxTweets() == extracted.getMaxTweets());
//        if (original.getNextTweet() != null) {
//            if (extracted.getNextTweet() != null) {
//                long mistake = original.getNextTweet().getTime() - extracted.getNextTweet().getTime();
//                long barrier = 1000;
//                assertTrue(mistake <= barrier && mistake >= -barrier);
//            } else {fail();}
//        } else {if (extracted.getNextTweet() != null) fail();}
//        assertTrue(original.getLastUpdateDay() == extracted.getLastUpdateDay());
//    }
//}
