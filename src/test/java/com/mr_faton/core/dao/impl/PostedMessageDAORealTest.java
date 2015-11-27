package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DBTestHelper;
import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.table.PostedMessage;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Description
 *
 * @author root
 * @since 14.10.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/test/daoTestConfig.xml"))
public class PostedMessageDAORealTest {
    private static final String TABLE = "posted_messages";
    private static final String COMMON_DATA_SET = "/test/data_set/posted_message/common.xml";
    private static final String EMPTY_POSTED_MESSAGES_TABLE = "/test/data_set/posted_message/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    PostedMessageDAO postedMessageDAO;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws Exception {
        DBTestHelper.fill(COMMON_DATA_SET, jdbcTemplate);
    }


    @Test
    public void getUnretweetedPostedMessage() throws Exception {
        ITable temp = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        System.out.println(temp);
        PostedMessage postedMessage = postedMessageDAO.getUnRetweetedPostedMessage();
        assertFalse(postedMessage.isRetweeted());
    }

    @Test
    public void saveAndUpdate() throws Exception {
        DBTestHelper.fill(EMPTY_POSTED_MESSAGES_TABLE, jdbcTemplate);
        String afterSave = "/test/data_set/posted_message/afterSave.xml";
        String afterUpdate = "/test/data_set/posted_message/afterUpdate.xml";

        PostedMessage postedMessage = new PostedMessage();
        postedMessage.setMessageId(2);
        postedMessage.setMessage("cssfg");
        postedMessage.setTwitterId(11646);

        postedMessage.setOldOwner("Zoe");
        postedMessage.setOldOwnerMale(false);
        postedMessage.setOldRecipient("Didi");
        postedMessage.setOldRecipientMale(true);

        postedMessage.setOwner("Sandy");
        postedMessage.setOwnerMale(false);
        postedMessage.setRecipient("Bob");
        postedMessage.setRecipientMale(true);

        postedMessage.setRetweeted(false);
        postedMessage.setPostedDate(TimeWizard.stringToDate("2015-06-25 19:59:10", DATE_PATTERN));

        ITable expected;
        ITable actual;
        String[] colsToIgnore = {"id"};

        //Test save
        postedMessageDAO.save(postedMessage);
        expected = DBTestHelper.getTableFromFile(TABLE, afterSave);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, colsToIgnore);

        //Test update
        ITable tempTable = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        int id = Integer.valueOf(tempTable.getValue(0, "id").toString());
        postedMessage.setId(id);
        postedMessage.setRetweeted(true);
        postedMessageDAO.update(postedMessage);
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, colsToIgnore);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        DBTestHelper.fill(EMPTY_POSTED_MESSAGES_TABLE, jdbcTemplate);
        String afterSaveList = "/test/data_set/posted_message/afterSaveList.xml";
        String afterUpdateList = "/test/data_set/posted_message/afterUpdateList.xml";

        PostedMessage postedMessage1 = new PostedMessage();
        postedMessage1.setMessageId(1);
        postedMessage1.setMessage("cksa");
        postedMessage1.setTwitterId(214684);
        postedMessage1.setOldOwner("Mandy");
        postedMessage1.setOldOwnerMale(false);
        postedMessage1.setOwner("Bob");
        postedMessage1.setRetweeted(false);
        postedMessage1.setPostedDate(TimeWizard.stringToDate("2014-11-12 15:28:45", DATE_PATTERN));

        PostedMessage postedMessage2 = new PostedMessage();
        postedMessage2.setMessageId(2);
        postedMessage2.setMessage("cssfg");
        postedMessage2.setTwitterId(1546);
        postedMessage2.setOldOwner("Zoe");
        postedMessage2.setOldOwnerMale(false);
        postedMessage2.setOldRecipient("Didi");
        postedMessage2.setOldRecipientMale(true);
        postedMessage2.setOwner("Sandy");
        postedMessage2.setOwnerMale(false);
        postedMessage2.setRecipient("Andy");
        postedMessage2.setRecipientMale(true);
        postedMessage2.setRetweeted(false);
        postedMessage2.setPostedDate(TimeWizard.stringToDate("2014-10-29 19:25:46", DATE_PATTERN));

        List<PostedMessage> postedMessageList = Arrays.asList(postedMessage1, postedMessage2);

        ITable expected;
        ITable actual;
        String[] colsToIgnore = {"id"};

        //Test save
        postedMessageDAO.save(postedMessageList);
        expected = DBTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, colsToIgnore);

        //Test update
        ITable tempTable = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        int postedMID1 = Integer.valueOf(tempTable.getValue(0, "id").toString());
        int postedMID2 = Integer.valueOf(tempTable.getValue(1, "id").toString());

        postedMessage1.setId(postedMID1);
        postedMessage1.setRetweeted(true);

        postedMessage2.setId(postedMID2);
        postedMessage2.setRetweeted(true);

        postedMessageDAO.update(postedMessageList);

        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);

        Assertion.assertEqualsIgnoreCols(expected, actual, colsToIgnore);
    }
}
