package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.TweetUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface TweetUserService {
    TweetUser getUserForTweet() throws SQLException, NoSuchEntityException;
    List<TweetUser> getUserList() throws SQLException, NoSuchEntityException;




    // INSERTS - UPDATES
    void save(TweetUser tweetUser) throws SQLException;
    void save(List<TweetUser> tweetUserList) throws SQLException;

    void update(TweetUser user) throws SQLException;
    void update(List<TweetUser> userList) throws SQLException;
}
