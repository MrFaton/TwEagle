package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.table.TweetUser;

import java.sql.SQLException;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface MessageDAO {
    Message getTweet(boolean male) throws SQLException;
    void updatePostedMessage(Message message) throws SQLException;
}
