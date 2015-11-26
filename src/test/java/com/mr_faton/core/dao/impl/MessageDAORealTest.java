package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DBTestHelper;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.Counter;

import java.sql.Time;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/test/daoTestConfig.xml"))
public class MessageDAORealTest {
    private static final String TABLE = "messages";
    private static final String COMMON_DATA_SET = "/test/data_set/message/common.xml";
    private static final String EMPTY_MESSAGES_TABLE = "/test/data_set/message/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws Exception {
        DBTestHelper.fill(COMMON_DATA_SET, jdbcTemplate);
    }


    @Test
    public void getAnyTweet() throws Exception {
        Message message = messageDAO.getTweet(true);
        Assert.assertTrue(message.isOwnerMale());
    }

    @Test
    public void getTweet() throws Exception {
        Calendar from = new GregorianCalendar(2013, 9, 23, 10, 5, 15); //month starts from 0, so if in Db it's 10 then here it's 9
        Calendar to = new GregorianCalendar(2013, 9, 23, 12, 10, 45);
        Message message = messageDAO.getTweet(true, from, to);
        Assert.assertTrue(message.isOwnerMale());
        Date min = from.getTime();
        Date max = to.getTime();
        Date postedDate = message.getPostedDate();
        Assert.assertTrue(postedDate.after(min) && postedDate.before(max));
        System.out.println(message.getMessage());
    }

    @Test
    public void saveAndUpdate() throws Exception {
        DBTestHelper.fill(EMPTY_MESSAGES_TABLE, jdbcTemplate);
        String afterSave = "/test/data_set/message/afterSave.xml";
        String afterUpdate = "/test/data_set/message/afterUpdate.xml";

        Message message = new Message();
        message.setMessage("tototo");
        message.setOwner("Tony");
        message.setOwnerMale(true);
        message.setRecipient("Frank");
        message.setRecipientMale(true);
        message.setPostedDate(TimeWizard.stringToDate("2014-03-25 14:25:40", DATE_PATTERN));
        message.setSynonymized(true);
        message.setPosted(false);

        ITable expected;
        ITable actual;
        String[] ignoringCol = {"id"};

        //Test save
        messageDAO.save(message);
        expected = DBTestHelper.getTableFromFile(TABLE, afterSave);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, ignoringCol);

        //Test update
        message.setId(1);
        message.setMessage("ljkdsjv");
        message.setSynonymized(false);
        message.setPosted(true);
        messageDAO.update(message);
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, ignoringCol);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        DBTestHelper.fill(EMPTY_MESSAGES_TABLE, jdbcTemplate);
        String afterSaveList = "/test/data_set/message/afterSaveList.xml";
        String afterUpdateList = "/test/data_set/message/afterUpdateList.xml";

        Message message1 = new Message();
        message1.setMessage("jvsdijs");
        message1.setOwner("Frank");
        message1.setOwnerMale(true);
        message1.setRecipient("Tony");
        message1.setRecipientMale(true);
        message1.setPostedDate(TimeWizard.stringToDate("2012-10-15 09:15:30", DATE_PATTERN));
        message1.setSynonymized(true);
        message1.setPosted(false);

        Message message2 = new Message();
        message2.setMessage("asdcu");
        message2.setOwner("Tony");
        message2.setOwnerMale(true);
        message2.setRecipient("Ann");
        message2.setRecipientMale(false);
        message2.setPostedDate(TimeWizard.stringToDate("2014-10-29 15:09:45", DATE_PATTERN));
        message2.setSynonymized(false);
        message2.setPosted(true);

        List<Message> messageList = Arrays.asList(message1, message2);

        ITable expected;
        ITable actual;
        String[] ignoringCol = {"id"};

        //Test save
        messageDAO.save(messageList);
        expected = DBTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, ignoringCol);

        //Test update
        message1.setId(1);
        message1.setMessage("casdc");
        message1.setSynonymized(false);
        message1.setPosted(true);

        message2.setId(2);
        message2.setMessage("dsag");
        message2.setSynonymized(true);
        message2.setPosted(false);

        messageDAO.update(messageList);

        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEqualsIgnoreCols(expected, actual, ignoringCol);
    }
}
