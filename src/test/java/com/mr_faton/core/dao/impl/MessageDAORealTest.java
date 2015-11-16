package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @AfterClass
    public static void tearDown() throws Exception {
        final String DELETE_FROM_MESSAGES_SQL = "" +
                "DELETE FROM tweagle.messages WHERE owner_id LIKE 'MessageDAOReal%';";
        final String DELETE_FROM_DONOR_USERS_SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE du_name LIKE 'MessageDAOReal%';";
        JDBC_TEMPLATE.update(DELETE_FROM_MESSAGES_SQL);
        JDBC_TEMPLATE.update(DELETE_FROM_DONOR_USERS_SQL);
    }

    @Test
    public void getTweetByOwnerGender() throws Exception {
        DonorUser owner = createDefaultDonorUser();
        owner.setMale(false);

        Message original = createDefaultMessage();
        original.setOwner(owner.getName());
        original.setOwnerMale(owner.isMale());

        DONOR_USER_DAO.save(owner);
        MESSAGE_DAO.save(original);

        Message extracted = MESSAGE_DAO.getTweet(owner.isMale());

        assertEquals(owner.isMale(), extracted.isOwnerMale());
    }

    @Test
    public void getTweetByOwnerGenderAndBetweenDates() throws Exception {
        DonorUser owner = createDefaultDonorUser();

        Message original = createDefaultMessage();
        original.setOwner(owner.getName());
        original.setOwnerMale(owner.isMale());

        DONOR_USER_DAO.save(owner);
        MESSAGE_DAO.save(original);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 2);

        Message extracted = MESSAGE_DAO.getTweet(owner.isMale(), min, max);

        assertEquals(owner.isMale(), extracted.isOwnerMale());
        assertTrue(min.getTime().before(extracted.getPostedDate()) && max.getTime().after(extracted.getPostedDate()));
    }

    @Test
    public void getTweetByOwnerName() throws Exception {
        DonorUser owner = createDefaultDonorUser();

        Message message = createDefaultMessage();
        message.setOwner(owner.getName());
        message.setOwnerMale(owner.isMale());

        DONOR_USER_DAO.save(owner);
        MESSAGE_DAO.save(message);

        Message received = MESSAGE_DAO.getTweet(owner.getName());

        equalsMessages(message, received);
    }

    @Test
    public void getTweetByOwnerNameAndBetweenDates() throws Exception {
        DonorUser owner = createDefaultDonorUser();

        Message original = createDefaultMessage();
        original.setOwner(owner.getName());
        original.setOwnerMale(owner.isMale());

        DONOR_USER_DAO.save(owner);
        MESSAGE_DAO.save(original);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 2);

        Message extracted = MESSAGE_DAO.getTweet(owner.getName(), min, max);

        equalsMessages(original, extracted);
    }

    @Test
    public void getMentionBetweenDates() throws Exception {
        DonorUser owner = createDefaultDonorUser();
        owner.setMale(false);

        DonorUser recipient = createDefaultDonorUser();
        recipient.setMale(false);

        Message original = createDefaultMessage();
        original.setOwner(owner.getName());
        original.setOwnerMale(owner.isMale());
        original.setRecipient(recipient.getName());
        original.setRecipientMale(recipient.isMale());

        DONOR_USER_DAO.save(Arrays.asList(owner, recipient));
        MESSAGE_DAO.save(original);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 2);

        Message extracted = MESSAGE_DAO.getMention(owner.isMale(), recipient.isMale(), min, max);

        assertEquals(owner.isMale(), extracted.isOwnerMale());
        assertEquals(recipient.isMale(), extracted.isRecipientMale());
        assertTrue(min.getTime().before(extracted.getPostedDate()) && max.getTime().after(extracted.getPostedDate()));
    }



    @Test
    public void saveAndUpdate() throws Exception {
        //Test save
        DonorUser owner = createDefaultDonorUser();
        DonorUser recipient = createDefaultDonorUser();

        Message original = createDefaultMessage();
        original.setOwner(owner.getName());
        original.setOwnerMale(owner.isMale());
        original.setRecipient(recipient.getName());
        original.setRecipientMale(recipient.isMale());


        DONOR_USER_DAO.save(Arrays.asList(owner, recipient));
        MESSAGE_DAO.save(original);

        Message extracted = getMessageByUniqueOwner(owner.getName());

        equalsMessages(original, extracted);

        //Test update
        original.setId(extracted.getId());
        original.setMessage("kajflsanf");
        original.setSynonymized(!original.isSynonymized());
        original.setPosted(!original.isPosted());

        MESSAGE_DAO.update(original);

        extracted = getMessageByUniqueOwner(original.getOwner());

        equalsMessages(original, extracted);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        //Test save
        DonorUser owner1 = createDefaultDonorUser();
        DonorUser recipient1 = createDefaultDonorUser();

        DonorUser owner2 = createDefaultDonorUser();
        DonorUser recipient2 = createDefaultDonorUser();

        Message original1 = createDefaultMessage();
        original1.setOwner(owner1.getName());
        original1.setOwnerMale(owner1.isMale());
        original1.setRecipient(recipient1.getName());
        original1.setRecipientMale(recipient1.isMale());

        Message original2 = createDefaultMessage();
        original2.setOwner(owner2.getName());
        original2.setOwnerMale(owner2.isMale());
        original2.setRecipient(recipient2.getName());
        original2.setRecipientMale(recipient2.isMale());

        List<Message> messageList = Arrays.asList(original1, original2);
        List<DonorUser> donorUserList = Arrays.asList(owner1, owner2, recipient1, recipient2);

        DONOR_USER_DAO.save(donorUserList);
        MESSAGE_DAO.save(messageList);

        Message extracted1 = getMessageByUniqueOwner(owner1.getName());
        Message extracted2 = getMessageByUniqueOwner(owner2.getName());

        equalsMessages(original1, extracted1);
        equalsMessages(original2, extracted2);


        //Test update
        original1.setId(extracted1.getId());
        original1.setMessage("fkdhjdofjoid");
        original1.setSynonymized(!original1.isSynonymized());
        original1.setPosted(!original1.isPosted());

        original2.setId(extracted2.getId());
        original2.setMessage("jhaenjer");
        original2.setSynonymized(!original2.isSynonymized());
        original2.setPosted(!original2.isPosted());

        MESSAGE_DAO.update(messageList);

        extracted1 = getMessageByUniqueOwner(owner1.getName());
        extracted2 = getMessageByUniqueOwner(owner2.getName());

        equalsMessages(original1, extracted1);
        equalsMessages(original2, extracted2);
    }


    private DonorUser createDefaultDonorUser() {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(BASE_NAME + Counter.getNextNumber());
        donorUser.setMale(true);
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
//        return JDBC_TEMPLATE.queryForObject(SQL, new MessageRowMapper());
        return null;
    }

    private void equalsMessages(Message original, Message extracted) {
        assertEquals(original.getMessage(), extracted.getMessage());
        assertEquals(original.getOwner(), extracted.getOwner());
        assertEquals(original.isOwnerMale(), extracted.isOwnerMale());
        assertEquals(original.getRecipient(), extracted.getRecipient());
        assertEquals(original.isRecipientMale(), extracted.isRecipientMale());
        assertEquals(original.isSynonymized(), extracted.isSynonymized());
        assertEquals(original.isPosted(), extracted.isPosted());
        long mistake = original.getPostedDate().getTime() - extracted.getPostedDate().getTime();
        long barrier = 1000;
        assertTrue(mistake <= barrier && mistake >= -barrier);
    }
}
