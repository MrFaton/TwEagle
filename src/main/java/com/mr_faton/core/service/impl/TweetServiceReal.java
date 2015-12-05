package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.TweetService;
import com.mr_faton.core.table.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public class TweetServiceReal implements TweetService {
    @Autowired
    private TweetDAO tweetDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Tweet getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        return tweetDAO.getTweet(ownerMale);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Tweet getTweet(boolean ownerMale, Date minDate, Date maxDate) throws SQLException, NoSuchEntityException {
        return tweetDAO.getTweet(ownerMale, minDate, maxDate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Tweet tweet) throws SQLException {
        tweetDAO.save(tweet);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<Tweet> tweetList) throws SQLException {
        tweetDAO.save(tweetList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Tweet tweet) throws SQLException {
        tweetDAO.update(tweet);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<Tweet> tweetList) throws SQLException {
        tweetDAO.update(tweetList);
    }
}
