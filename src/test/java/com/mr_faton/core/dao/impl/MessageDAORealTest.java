package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.table.Message;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class MessageDAORealTest {
    private static final String BASE_NAME = "MessageDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
    private static final MessageDAO MESSAGE_DAO = (MessageDAO) AppContext.getBeanByName("messageDAO");

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.messages WHERE owner_id LIKE 'MessageDAOReal%';";
        JDBC_TEMPLATE.update(SQL);
    }

    @Test
    public void saveAndUpdate() throws Exception {

        Message message = createDefaultMessage();

        MESSAGE_DAO.save(message);

        String text = "saveAndUpdate";

    }

    private Message createDefaultMessage() {
        Message message = new Message();

        message.setMessage("Test message");

        message.setOwner(BASE_NAME + Counter.getNextNumber());
        message.setOwnerMale(true);

        message.setRecipient(null);
        message.setRecipientMale(false);

        message.setPostedDate(new Date());

        message.setSynonymized(false);
        message.setPosted(false);

        return message;
    }
}
