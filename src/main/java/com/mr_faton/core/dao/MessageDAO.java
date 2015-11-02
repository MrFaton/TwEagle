package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface MessageDAO {
    Message getTweet(boolean male, String ownerName, Calendar minCalendar, Calendar maxCalendar)
        throws SQLException, NoSuchEntityException;
    Message getMention(boolean tweet, boolean male, String ownerName, String recipientName,
                       Calendar minCalendar, Calendar maxCalendar) throws SQLException, NoSuchEntityException;




    //Get UnSynonymized message
    List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException;


    // INSERTS - UPDATES
    void save(Message message) throws SQLException;
    void save(List<Message> messageList) throws SQLException;

    void update(Message message) throws SQLException;
    void update(List<Message> messageList) throws SQLException;
}
