package com.mr_faton.core.task.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.DataSequenceReachedException;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;
import util.Counter;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 20.10.2015
 */
public class MessageParseTaskTest {
    private static final String BASE_NAME = "MessageParseTask";
    private DonorUserDAO donorUserDAO;
    private MessageParseTask messageParseTask;

    @Before
    public void setUp() {
        donorUserDAO = mock(DonorUserDAO.class);
//        messageParseTask = new MessageParseTask(mock(TwitterAPI.class), mock(MessageDAO.class), donorUserDAO);
    }

    @Test
    public void update() throws Exception {
        DonorUser donorUser = createDonorUser();
        when(donorUserDAO.getDonorForMessage()).thenReturn(donorUser);

        messageParseTask.update();

        Field donorUserField = messageParseTask.getClass().getDeclaredField("donorUser");
        donorUserField.setAccessible(true);
        DonorUser extractedDonorUser = (DonorUser) donorUserField.get(messageParseTask);

        assertTrue(donorUser == extractedDonorUser);
    }

    @Test
    public void mentionValidator() {
        String mention1 = "@SomeOne hello! How are you?";
        String mention2 = "@SomeOne @Ted hello! How are you?";
        String mention3 = "@SomeOne @ hello! How are you?";

        assertTrue(messageParseTask.mentionValidator(mention1));
        assertFalse(messageParseTask.mentionValidator(mention2));
        assertFalse(messageParseTask.mentionValidator(mention3));
    }

    @Test
    public void handleUserTimeLineList() throws NoSuchFieldException, IllegalAccessException {
        DonorUser donorUser = createDonorUser();
        Field donorUserField = messageParseTask.getClass().getDeclaredField("donorUser");
        donorUserField.setAccessible(true);
        donorUserField.set(messageParseTask, donorUser);

        Status status1 = createStatus();

        Status status2 = createStatus();
        when(status2.getLang()).thenReturn("en");

        Status status3 = createStatus();
        when(status3.isRetweet()).thenReturn(true);

        Status status4 = createStatus();
        when(status4.getText()).thenReturn("hello this is commercial http:\\\\test.com");

        Status status5 = createStatus();
        when(status5.getText()).thenReturn("@Test @ this is bad mention");

        Status status6 = createStatus();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -5);
        when(status6.getCreatedAt()).thenReturn(new Date(calendar.getTimeInMillis()));



        Iterator<Status> statusIterator = mock(Iterator.class);
        when(statusIterator.hasNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(statusIterator.next())
                .thenReturn(status1)
                .thenReturn(status2)
                .thenReturn(status3)
                .thenReturn(status4)
                .thenReturn(status5)
                .thenReturn(status6);


        ResponseList<Status> responseList = mock(ResponseList.class);
        when(responseList.size()).thenReturn(2);
        when(responseList.iterator()).thenReturn(statusIterator);

        try {
            messageParseTask.handleUserTimeLineList(responseList);
        } catch (DataSequenceReachedException e) {
            /*NOP*/
        }

        Field messageListField = messageParseTask.getClass().getDeclaredField("messageList");
        messageListField.setAccessible(true);
        List<Message> extractedMessageList = (List) messageListField.get(messageParseTask);

        assertEquals(1, extractedMessageList.size());
    }


    DonorUser createDonorUser() {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(BASE_NAME + Counter.getNextNumber());
        donorUser.setMale(true);
        return donorUser;
    }

    Status createStatus() {
        Status status = mock(Status.class);
        when(status.getCreatedAt()).thenReturn(new Date());
        when(status.getLang()).thenReturn("ru");
        when(status.isRetweet()).thenReturn(false);
        when(status.getText()).thenReturn("status");
        return status;
    }
}
