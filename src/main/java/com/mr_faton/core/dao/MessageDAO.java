package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface MessageDAO {
    // Get Tweet
    Message getTweetFirstTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getTweetSecondTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getTweetThirdTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getAnyTweet(boolean male) throws SQLException;

    //Get UnSynonymized message
    List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException;


    // INSERTS - UPDATES
    void save(Message message) throws SQLException;
    void save(List<Message> messageList) throws SQLException;

    void update(Message message) throws SQLException;
    void update(List<Message> messageList) throws SQLException;
}
