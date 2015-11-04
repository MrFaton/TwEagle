package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import util.Counter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
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
    private static final DonorUserDAO DONOR_USER_DAO = (DonorUserDAO) AppContext.getBeanByName("donorUserDAO");

//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String DELETE_FROM_MESSAGES_SQL = "" +
//                "DELETE FROM tweagle.messages WHERE owner_id LIKE 'MessageDAOReal%';";
//        final String DELETE_FROM_DONOR_USERS_SQL = "" +
//                "DELETE FROM tweagle.donor_users WHERE du_name LIKE 'MessageDAOReal%';";
//        JDBC_TEMPLATE.update(DELETE_FROM_MESSAGES_SQL);
//        JDBC_TEMPLATE.update(DELETE_FROM_DONOR_USERS_SQL);
//    }

    @Test
    public void getTweetByOwnerMale() throws Exception {
        MESSAGE_DAO.getMention(true, true, Calendar.getInstance(), Calendar.getInstance());
    }

    @Test
    public void saveAndUpdate() throws Exception {
        Message message = createDefaultMessage();

        DonorUser owner = new DonorUser();
        owner.setName(message.getOwner());
        owner.setMale(message.isOwnerMale());

        DonorUser recipient = new DonorUser();
        recipient.setName(message.getRecipient());
        recipient.setMale(message.isRecipientMale());


        DONOR_USER_DAO.save(Arrays.asList(owner, recipient));
        MESSAGE_DAO.save(message);

        message.setMessage("saveAndUpdate");
        message.setSynonymized(false);
        message.setPosted(false);


        MESSAGE_DAO.update(message);

    }

    private Message createDefaultMessage() {
        Message message = new Message();

        message.setMessage("Test message " + Counter.getNextNumber());

        message.setOwner(BASE_NAME + Counter.getNextNumber());
        message.setOwnerMale(true);

        message.setRecipient(BASE_NAME + Counter.getNextNumber());
        message.setRecipientMale(true);

        message.setPostedDate(new Date());

        message.setSynonymized(true);
        message.setPosted(true);

        return message;
    }

    private Message getMessageByUniqueOwner(String ownerName) {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "owner.du_name AS 'owner', owner.du_male AS 'owner_male', " +
                "recipient.du_name AS 'recipient', recipient.du_male AS 'recipient_male' " +
                "FROM tweagle.messages " +
                "INNER JOIN tweagle.donor_users owner ON messages.owner_id = owner.du_name " +
                "INNER JOIN tweagle.donor_users recipient ON messages.recipient_id = recipient.du_name " +
                "WHERE owner.du_name = '" + ownerName + "';";
        return JDBC_TEMPLATE.queryForObject(SQL, new MessageRowMapper());
    }

    class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet resultSet, int roeNum) throws SQLException {
            Message message = new Message();

            message.setId(resultSet.getInt("id"));
            message.setMessage(resultSet.getString("message"));

            message.setOwner(resultSet.getString("owner"));
            message.setOwnerMale(resultSet.getBoolean("owner_male"));

            message.setRecipient(resultSet.getString("recipient"));
            message.setRecipientMale(resultSet.getBoolean("recipient_male"));

            message.setPostedDate(new Date(resultSet.getTimestamp("posted_date").getTime()));

            message.setSynonymized(resultSet.getBoolean("synonymized"));
            message.setPosted(resultSet.getBoolean("posted"));

            return message;
        }
    }
}
