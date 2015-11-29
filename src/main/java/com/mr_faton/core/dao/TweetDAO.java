package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Tweet;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
public interface TweetDAO {
    Tweet getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException;
    Tweet getTweet(boolean ownerMale, Date minDate, Date maxDate) throws SQLException, NoSuchEntityException;

    void save(Tweet tweet) throws SQLException;
    void save(List<Tweet> tweetList) throws SQLException;

    void update(Tweet tweet) throws SQLException;
    void update(List<Tweet> tweetList) throws SQLException;
}
