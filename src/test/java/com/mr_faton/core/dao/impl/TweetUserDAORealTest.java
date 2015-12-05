package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.table.TweetUser;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import util.DBTestHelper;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
@Transactional
public class TweetUserDAORealTest {
    private static final String TABLE = "tweet_users";
    private static final String COMMON_DATA_SET = "/data_set/tweet_user/common.xml";
    private static final String EMPTY_TABLE = "/data_set/tweet_user/empty.xml";

    @Autowired
    TweetUserDAO tweetUserDAO;
    @Autowired
    DBTestHelper dbTestHelper;

    @Before
    public void setUp() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }

    @Test
    public void getUserForTweet() throws Exception {
        System.out.println(dbTestHelper.tableAsString(dbTestHelper.getTableFromSchema(TABLE)));
        TweetUser tweetUser = tweetUserDAO.getUserForTweet();
        assertTrue(tweetUser.isTweet());
        assertTrue(tweetUser.getCurTweets() < tweetUser.getMaxTweets());
    }

    @Test
    public void getUserList() throws Exception {
        List<TweetUser> tweetUserList = tweetUserDAO.getUserList();
        assertTrue(tweetUserList.size() == 3);
    }

    @Test
    public void saveAndUpdate() throws Exception {
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSave = "/data_set/tweet_user/afterSave.xml";
        String afterUpdate = "/data_set/tweet_user/afterUpdate.xml";

        TweetUser tweetUser = new TweetUser();
        tweetUser.setName("UserB");
        tweetUser.setTweet(true);
        tweetUser.setCurTweets(1);
        tweetUser.setMaxTweets(3);
        tweetUser.setLastUpdateDay(2);
        tweetUser.setNextTweet(TimeWizard.stringToDate("2015-10-17 21:45:39", DBTestHelper.DATE_TIME_PATTERN));

        ITable expected;
        ITable actual;

        //Test save
        tweetUserDAO.save(tweetUser);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSave);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);

        //Test update
        tweetUser.setId(dbTestHelper.getId(TABLE, 0));
        tweetUser.setTweet(false);
        tweetUser.setCurTweets(3);
        tweetUser.setMaxTweets(5);
        tweetUser.setLastUpdateDay(8);
        tweetUser.setNextTweet(TimeWizard.stringToDate("2015-12-03 10:16:14", DBTestHelper.DATE_TIME_PATTERN));

        tweetUserDAO.update(tweetUser);
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSaveList = "/data_set/tweet_user/afterSaveList.xml";
        String afterUpdateList = "/data_set/tweet_user/afterUpdateList.xml";

        TweetUser tweetUser1 = new TweetUser();
        tweetUser1.setName("UserB");
        tweetUser1.setTweet(true);
        tweetUser1.setCurTweets(1);
        tweetUser1.setMaxTweets(5);
        tweetUser1.setLastUpdateDay(10);
        tweetUser1.setNextTweet(TimeWizard.stringToDate("2015-12-03 10:49:18", DBTestHelper.DATE_TIME_PATTERN));

        TweetUser tweetUser2 = new TweetUser();
        tweetUser2.setName("UserA");
        tweetUser2.setTweet(true);
        tweetUser2.setCurTweets(2);
        tweetUser2.setMaxTweets(10);
        tweetUser2.setLastUpdateDay(20);
        tweetUser2.setNextTweet(TimeWizard.stringToDate("2015-06-14 22:44:55", DBTestHelper.DATE_TIME_PATTERN));

        List<TweetUser> tweetUserList = Arrays.asList(tweetUser1, tweetUser2);

        ITable expected;
        ITable actual;

        //Test save
        tweetUserDAO.save(tweetUserList);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);

        //Test update
        tweetUser1.setId(dbTestHelper.getId(TABLE, 1));
        tweetUser1.setTweet(false);
        tweetUser1.setCurTweets(5);
        tweetUser1.setMaxTweets(15);
        tweetUser1.setLastUpdateDay(22);
        tweetUser1.setNextTweet(TimeWizard.stringToDate("2014-03-07 13:19:28", DBTestHelper.DATE_TIME_PATTERN));

        tweetUser2.setId(dbTestHelper.getId(TABLE, 0));
        tweetUser2.setTweet(false);
        tweetUser2.setCurTweets(18);
        tweetUser2.setMaxTweets(22);
        tweetUser2.setLastUpdateDay(28);
        tweetUser2.setNextTweet(TimeWizard.stringToDate("2014-11-18 20:40:50", DBTestHelper.DATE_TIME_PATTERN));

        tweetUserDAO.update(tweetUserList);
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
