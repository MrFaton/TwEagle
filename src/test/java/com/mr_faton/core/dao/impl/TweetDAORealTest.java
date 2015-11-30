package com.mr_faton.core.dao.impl;

import util.DBTestHelper;
import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.table.Tweet;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
public class TweetDAORealTest {
    private static final String TABLE = "tweets";
    private static final String COMMON_DATA_SET = "/data_set/tweet/common.xml";
    private static final String EMPTY_TWEETS_TABLE = "/data_set/tweet/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private TweetDAO tweetDAO;
    @Autowired
    private DBTestHelper dbTestHelper;

    @Before
    public void before() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }

    @Test
    public void getTweetByOwnerMale() throws Exception {
        Tweet tweet = tweetDAO.getTweet(false);
        assertFalse(tweet.isOwnerMale());
        assertTrue(tweet.isSynonymized());
        assertFalse(tweet.isReposted());
    }

    @Test
    public void getTweetByOwnerMaleAndBetweenDates() throws Exception {
        Date minDate = TimeWizard.stringToDate("2012-03-28 07:00:00", DATE_PATTERN);
        Date maxDate = TimeWizard.stringToDate("2012-03-28 08:00:00", DATE_PATTERN);
        Tweet tweet = tweetDAO.getTweet(false, minDate, maxDate);
        assertFalse(tweet.isOwnerMale());
        Date actualDate = tweet.getPostedDate();
        assertTrue(actualDate.after(minDate) && actualDate.before(maxDate));
        assertTrue(tweet.isSynonymized());
        assertFalse(tweet.isReposted());
    }


    @Test
    public void saveAndUpdate() throws Exception {
        dbTestHelper.fill(EMPTY_TWEETS_TABLE);
        String afterSave = "/data_set/tweet/afterSave.xml";
        String afterUpdate = "/data_set/tweet/afterUpdate.xml";

        Tweet tweet = new Tweet();
        tweet.setOwner("donorUserA");
        tweet.setMessage("kvsdrvckds");
        tweet.setPostedDate(TimeWizard.stringToDate("2015-10-16 09:21:26", DATE_PATTERN));

        ITable expected;
        ITable actual;

        //Test save
        tweetDAO.save(tweet);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSave);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);

        //Test update
        tweet.setId(dbTestHelper.getId(TABLE, 0));
        tweet.setMessage("cjsoidvj");
        tweet.setSynonymized(true);
        tweet.setReposted(true);
        tweetDAO.update(tweet);
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        dbTestHelper.fill(EMPTY_TWEETS_TABLE);
        String afterSaveList = "/data_set/tweet/afterSaveList.xml";
        String afterUpdateList = "/data_set/tweet/afterUpdateList.xml";

        Tweet tweet1 = new Tweet();
        tweet1.setOwner("donorUserA");
        tweet1.setMessage("vksd");
        tweet1.setPostedDate(TimeWizard.stringToDate("2012-10-23 21:03:45", DATE_PATTERN));
        tweet1.setSynonymized(false);
        tweet1.setReposted(false);

        Tweet tweet2 = new Tweet();
        tweet2.setOwner("donorUserB");
        tweet2.setMessage("vkjsd");
        tweet2.setPostedDate(TimeWizard.stringToDate("2014-08-14 15:41:19", DATE_PATTERN));
        tweet2.setSynonymized(false);
        tweet2.setReposted(false);

        List<Tweet> tweetList = new ArrayList<>(2);
        tweetList.add(tweet1);
        tweetList.add(tweet2);

        ITable expected;
        ITable actual;

        //Test save
        tweetDAO.save(tweetList);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);

        //Test update
        tweet1.setId(dbTestHelper.getId(TABLE, 0));
        tweet1.setMessage("lntp");
        tweet1.setSynonymized(true);
        tweet1.setReposted(true);

        tweet2.setId(dbTestHelper.getId(TABLE, 1));
        tweet2.setMessage("csatsa6");
        tweet2.setSynonymized(true);
        tweet2.setReposted(true);

        tweetDAO.update(tweetList);
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
