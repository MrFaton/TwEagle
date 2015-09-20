package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.TweetUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface TweetUserDAO {
    TweetUser getUserForTweet() throws SQLException, NoSuchEntityException;
    List<TweetUser> getUserList() throws SQLException, NoSuchEntityException;

    void updateUser(TweetUser user) throws SQLException;
    void updateUserList(List<TweetUser> userList) throws SQLException;

    void addUser(TweetUser user) throws SQLException;
}
