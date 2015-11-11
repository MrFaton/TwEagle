package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.TweetUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 */
public interface TweetUserDAO {
    TweetUser getUserForTweet() throws SQLException, NoSuchEntityException;
    List<TweetUser> getUserList() throws SQLException, NoSuchEntityException;




    // INSERTS - UPDATES
    void save(TweetUser tweetUser) throws SQLException;
    void save(List<TweetUser> tweetUserList) throws SQLException;

    void update(TweetUser user) throws SQLException;
    void update(List<TweetUser> userList) throws SQLException;
}
