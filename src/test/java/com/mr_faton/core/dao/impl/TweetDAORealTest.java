package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DBTestHelper;
import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.table.Tweet;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/test/daoTestConfig.xml"))
public class TweetDAORealTest {
    private static final String TABLE = "tweets";
    private static final String COMMON_DATA_SET = "/test/data_set/tweet/common.xml";
    private static final String EMPTY_TWEETS_TABLE = "/test/data_set/tweet/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private TweetDAO tweetDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws Exception {
        DBTestHelper.fill(COMMON_DATA_SET, jdbcTemplate);
    }




    @Test
    public void saveAndUpdate() throws Exception {
        DBTestHelper.fill(EMPTY_TWEETS_TABLE, jdbcTemplate);
        String afterSave = "/test/data_set/tweet/afterSave.xml";
        String afterUpdate = "/test/data_set/tweet/afterUpdate.xml";

        Tweet tweet = new Tweet();
        tweet.setId(1);
        tweet.setOwner("donorUserA");
        tweet.setMessage("kvsdrvckds");
        tweet.setPostedDate(TimeWizard.stringToDate("2015-10-16 09:21:26", DATE_PATTERN));

        ITable expected;
        ITable actual;

        //Test save
        tweetDAO.save(tweet);
        expected = DBTestHelper.getTableFromFile(TABLE, afterSave);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

        //Test update
        tweet.setMessage("cjsoidvj");
        tweet.setSynonymized(true);
        tweet.setReposted(true);
        tweetDAO.update(tweet);
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        DBTestHelper.fill(EMPTY_TWEETS_TABLE, jdbcTemplate);
        String afterSaveList = "/test/data_set/tweet/afterSaveList.xml";
        String afterUpdateList = "/test/data_set/tweet/afterUpdateList.xml";

        Tweet tweet1 = new Tweet();
        tweet1.setId(1);
        tweet1.setOwner("donorUserA");
        tweet1.setMessage("vksd");
        tweet1.setPostedDate(TimeWizard.stringToDate("2012-10-23 21:03:45", DATE_PATTERN));
        tweet1.setSynonymized(false);
        tweet1.setReposted(false);

        Tweet tweet2 = new Tweet();
        tweet2.setId(2);
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
        expected = DBTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

        //Test update
        tweet1.setMessage("lntp");
        tweet1.setSynonymized(true);
        tweet1.setReposted(true);

        tweet2.setMessage("csatsa6");
        tweet2.setSynonymized(true);
        tweet2.setReposted(true);

        tweetDAO.update(tweetList);
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
    }

}
