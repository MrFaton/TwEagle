package com.mr_faton.core.api;

import com.mr_faton.core.exception.BadUserException;
import com.mr_faton.core.exception.NoSuchEntityException;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.sql.SQLException;

public interface TwitterAPI{

    long postTweet(String userName, String tweet) throws TwitterException, SQLException, NoSuchEntityException;

    void deleteTweetById(String userName, long id) throws TwitterException, SQLException, NoSuchEntityException;

    void deleteLastTweet(String userName) throws TwitterException, SQLException, NoSuchEntityException;

    ResponseList<Status> getUserTimeLine(String donorUserName, Paging paging, String sourceUserName)
            throws TwitterException, SQLException, NoSuchEntityException, BadUserException;
}
