package com.mr_faton.core.api;

import com.mr_faton.core.exception.NoSuchEntityException;
import twitter4j.*;

import java.sql.SQLException;

public interface TwitterAPI{

    long postTweet(String userName, String tweet) throws TwitterException, SQLException, NoSuchEntityException;

    void deleteTweetById(String userName, long id) throws TwitterException, SQLException, NoSuchEntityException;

    void deleteLastTweet(String userName) throws TwitterException, SQLException, NoSuchEntityException;
}
