package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 12.11.2015
 */
public interface MessageService {
    //Get Tweet
    Message getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException;

    Message getTweet(boolean ownerMale, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException;

    Message getTweet(String ownerName) throws SQLException, NoSuchEntityException;

    Message getTweet(String ownerName, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException;


    //Get Mention
    Message getMention(boolean ownerMale, boolean recipientMale, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException;




    //Get UnSynonymized message
    List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException;


    // INSERTS - UPDATES
    void save(Message message) throws SQLException;
    void save(List<Message> messageList) throws SQLException;

    void update(Message message) throws SQLException;
    void update(List<Message> messageList) throws SQLException;
}
