package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.table.TweetUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface MessageDAO {
    Message getTweetFirstTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getTweetSecondTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getTweetThirdTry(boolean male) throws SQLException, NoSuchEntityException;
    Message getAnyTweet(boolean male) throws SQLException;

    void updatePostedMessage(Message message) throws SQLException;

    void saveMessageList(List<Message> messageList) throws SQLException;
}
