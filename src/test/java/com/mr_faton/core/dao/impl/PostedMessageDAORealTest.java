package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import static org.junit.Assert.*;

import com.mr_faton.core.exception.NoSuchEntityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.DBTestHelper;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 30.11.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
public class PostedMessageDAORealTest {
    private static final String TABLE = "posted_messages";
    private static final String COMMON_DATA_SET = "/data_set/posted_message/common.xml";
    private static final String EMPTY_TWEETS_TABLE = "/data_set/posted_message/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    PostedMessageDAO postedMessageDAO;
    @Autowired
    DBTestHelper dbTestHelper;

    @Before
    public void before() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }

    @Test
    public void getTwitterId() throws Exception {
        long actualTwitterId = postedMessageDAO.getTwitterId();
        assertTrue(actualTwitterId == 235431651125L || actualTwitterId == 135415454154L);
    }

    @Test
    public void save() throws Exception {
        dbTestHelper.fill(EMPTY_TWEETS_TABLE);
        long expectedId = 165441654L;
        postedMessageDAO.save(expectedId);
        long actualId = postedMessageDAO.getTwitterId();
        assertTrue(expectedId == actualId);
    }

    @Test(expected = NoSuchEntityException.class)
    public void delete() throws Exception {
        postedMessageDAO.delete(235431651125L);
        postedMessageDAO.delete(135415454154L);
        postedMessageDAO.getTwitterId();
    }
}
