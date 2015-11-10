package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.table.PostedMessage;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.TimeWizard;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.sql.SQLException;
import java.util.*;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class PostedMessageDAORealTest {
    private static final String BASE_NAME = "PostedMessageDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
    private static final UserDAO USER_DAO = (UserDAO) AppContext.getBeanByName("userDAO");
    private static final DonorUserDAO DONOR_USER_DAO = (DonorUserDAO) AppContext.getBeanByName("donorUserDAO");
    private static final MessageDAO MESSAGE_DAO = (MessageDAO) AppContext.getBeanByName("messageDAO");
    private static final PostedMessageDAO POSTED_MESSAGE_DAO = (PostedMessageDAO) AppContext.getBeanByName("postedMessageDAO");

    @AfterClass
    public static void tearDown() throws Exception {
        final String deletePattern = BASE_NAME + "%";
        final String SQL_DELETE_FROM_POSTED_MESSAGES = "" +
                "DELETE FROM tweagle.posted_messages WHERE owner_id LIKE '" + deletePattern + "';";
        final String SQL_DELETE_FROM_MESSAGES = "" +
                "DELETE FROM tweagle.messages WHERE owner_id LIKE '" + deletePattern + "';";
        final String SQL_DELETE_FROM_DONOR_USERS = "" +
                "DELETE FROM tweagle.donor_users WHERE du_name LIKE '" + deletePattern + "';";
        final String SQL_DELETE_FROM_USERS = "" +
                "DELETE FROM tweagle.users WHERE u_name LIKE '" + deletePattern + "';";

        JDBC_TEMPLATE.batchUpdate(
                SQL_DELETE_FROM_POSTED_MESSAGES,
                SQL_DELETE_FROM_MESSAGES,
                SQL_DELETE_FROM_DONOR_USERS,
                SQL_DELETE_FROM_USERS);
    }

    @Test
    public void getUnRetweetedPostedMessage() throws Exception {
        PostedMessage original = createDefaultPostedMessage();
        original.setRetweeted(false);

        POSTED_MESSAGE_DAO.save(original);

        PostedMessage extracted = POSTED_MESSAGE_DAO.getUnRetweetedPostedMessage();

        assertFalse(extracted.isRetweeted());
    }


    @Test
    public void saveAndUpdate() throws Exception {
        PostedMessage original = createDefaultPostedMessage();

        //Test save
        POSTED_MESSAGE_DAO.save(original);
        PostedMessage extracted = getPostedMessageByUniqueOwner(original.getOwner());
        equalsPostedMessages(original, extracted);

        //Test update
        original.setRetweeted(!original.isRetweeted());
        POSTED_MESSAGE_DAO.update(original);
        extracted = getPostedMessageByUniqueOwner(original.getOwner());
        equalsPostedMessages(original, extracted);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        PostedMessage original1 = createDefaultPostedMessage();
        PostedMessage original2 = createDefaultPostedMessage();
        List<PostedMessage> postedMessageList = Arrays.asList(original1, original2);

        PostedMessage extracted1;
        PostedMessage extracted2;

        //Test save
        POSTED_MESSAGE_DAO.save(postedMessageList);

        extracted1 = getPostedMessageByUniqueOwner(original1.getOwner());
        extracted2 = getPostedMessageByUniqueOwner(original2.getOwner());

        equalsPostedMessages(original1, extracted1);
        equalsPostedMessages(original2, extracted2);

        //Test update
        original1.setRetweeted(!original1.isRetweeted());
        original2.setRetweeted(!original2.isRetweeted());

        POSTED_MESSAGE_DAO.update(postedMessageList);

        extracted1 = getPostedMessageByUniqueOwner(original1.getOwner());
        extracted2 = getPostedMessageByUniqueOwner(original2.getOwner());

        equalsPostedMessages(original1, extracted1);
        equalsPostedMessages(original2, extracted2);
    }



    private User createDefaultUser() {
        User user = new User();

        user.setName(BASE_NAME + Counter.getNextNumber());
        user.setPassword("secret");
        user.setEmail("testmail@test.com");
        user.setMale(true);
        user.setCreationDate(new Date());
        user.setMessages(100);
        user.setFollowing(200);
        user.setFollowers(300);
        user.setConsumerKey("consumer key");
        user.setConsumerSecret("consumer secret");
        user.setAccessToken("access token");
        user.setAccessTokenSecret("access token secret");

        return user;
    }

    private DonorUser createDefaultDonorUser() {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(BASE_NAME + Counter.getNextNumber());
        donorUser.setMale(true);
        donorUser.setTakeMessageDate(new Date(System.currentTimeMillis() - 1000));
        donorUser.setTakeFollowingDate(new Date(System.currentTimeMillis() - 2000));
        donorUser.setTakeFollowersDate(new Date(System.currentTimeMillis() - 3000));
        return donorUser;
    }

    private Message createDefaultMessage() {
        Message message = new Message();

        message.setMessage("Test message " + Counter.getNextNumber());

        message.setPostedDate(new Date());

        message.setSynonymized(true);
        message.setPosted(true);

        return message;
    }

    private Message getMessageByUniqueOwner(String ownerName) {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "owner.du_name AS 'owner', owner.male AS 'owner_male', " +
                "recipient.du_name AS 'recipient', recipient.male AS 'recipient_male' " +
                "FROM tweagle.messages " +
                "INNER JOIN tweagle.donor_users owner ON messages.owner_id = owner.du_name " +
                "INNER JOIN tweagle.donor_users recipient ON messages.recipient_id = recipient.du_name " +
                "WHERE owner.du_name = '" + ownerName + "';";
        return JDBC_TEMPLATE.queryForObject(SQL, new MessageRowMapper());
    }

    private PostedMessage getPostedMessageByUniqueOwner(String ownerName) throws NoSuchEntityException {
        final String SQL = "" +
                "SELECT " +
                "posted_messages.id, " +

                "posted_messages.message_id, " +
                "messages.message, " +

                "posted_messages.twitter_id, " +

                "oldOwnerTable.du_name AS 'oldOwner', " +
                "oldOwnerTable.male AS 'oldOwnerMale', " +

                "oldRecipientTable.du_name AS 'oldRecipient', " +
                "oldRecipientTable.male AS 'oldRecipientMale', " +

                "posted_messages.owner_id, " +
                "ownerTable.male AS 'ownerMale', " +

                "posted_messages.recipient_id, " +
                "recipientTable.male AS 'recipientMale', " +

                "posted_messages.retweeted, " +
                "posted_messages.posted_date " +

                "FROM tweagle.posted_messages " +
                "INNER JOIN tweagle.messages ON posted_messages.message_id = messages.id " +
                "INNER JOIN tweagle.donor_users oldOwnerTable ON tweagle.messages.owner_id = oldOwnerTable.du_name " +
                "INNER JOIN tweagle.donor_users oldRecipientTable ON tweagle.messages.recipient_id = oldRecipientTable.du_name " +
                "INNER JOIN tweagle.users ownerTable ON posted_messages.owner_id = ownerTable.u_name " +
                "INNER JOIN tweagle.users recipientTable ON posted_messages.recipient_id = recipientTable.u_name " +
                "WHERE posted_messages.owner_id = '" + ownerName + "';";

        try {
            return JDBC_TEMPLATE.queryForObject(SQL, new PostedMessageRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("no posted message", emptyData);
        }
    }

    private PostedMessage createDefaultPostedMessage() throws SQLException {
        DonorUser oldOwner = createDefaultDonorUser();
        oldOwner.setMale(false);
        DonorUser oldRecipient = createDefaultDonorUser();
        oldRecipient.setMale(true);

        User owner = createDefaultUser();
        owner.setMale(true);
        User recipient = createDefaultUser();
        recipient.setMale(false);

        Message message = createDefaultMessage();
        message.setOwner(oldOwner.getName());
        message.setRecipient(oldRecipient.getName());

        DONOR_USER_DAO.save(Arrays.asList(oldOwner, oldRecipient));
        USER_DAO.save(Arrays.asList(owner, recipient));
        MESSAGE_DAO.save(message);

        message.setId(getMessageByUniqueOwner(oldOwner.getName()).getId());

        PostedMessage postedMessage = new PostedMessage();
        postedMessage.setMessageId(message.getId());
        postedMessage.setMessage(message.getMessage());
        postedMessage.setTwitterId(Counter.getNextNumber());

        postedMessage.setOldOwner(oldOwner.getName());
        postedMessage.setOldOwnerMale(oldOwner.isMale());
        postedMessage.setOldRecipient(oldRecipient.getName());
        postedMessage.setOldRecipientMale(oldRecipient.isMale());

        postedMessage.setOwner(owner.getName());
        postedMessage.setOwnerMale(owner.isMale());
        postedMessage.setRecipient(recipient.getName());
        postedMessage.setRecipientMale(recipient.isMale());

        postedMessage.setRetweeted(false);
        postedMessage.setPostedDate(new Date());

        return postedMessage;
    }

    private void equalsPostedMessages(PostedMessage original, PostedMessage extracted) {
        assertTrue(original.getMessageId() == extracted.getMessageId());
        assertEquals(original.getMessage(), extracted.getMessage());
        assertTrue(original.getTwitterId() == extracted.getTwitterId());

        assertEquals(original.getOldOwner(), extracted.getOldOwner());
        assertTrue(original.isOldOwnerMale() == extracted.isOldOwnerMale());
        assertEquals(original.getOldRecipient(), extracted.getOldRecipient());
        assertTrue(original.isOldRecipientMale() == extracted.isOldRecipientMale());

        assertEquals(original.getOwner(), extracted.getOwner());
        assertTrue(original.isOwnerMale() == extracted.isOwnerMale());
        assertEquals(original.getRecipient(), extracted.getRecipient());
        assertTrue(original.isRecipientMale() == extracted.isRecipientMale());

        assertTrue(original.isRetweeted() == extracted.isRetweeted());
        long mistake = original.getPostedDate().getTime() - extracted.getPostedDate().getTime();
        long barrier = 1000;
        assertTrue(mistake <= barrier && mistake >= -barrier);
    }
}
