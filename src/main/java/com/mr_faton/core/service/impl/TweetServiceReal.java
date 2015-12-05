package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.TweetService;
import com.mr_faton.core.table.Tweet;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.TweetServiceReal");

    @Autowired
    private TweetDAO tweetDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Tweet getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        logger.debug("begin search tweet with parameters: owner male = " + ownerMale);
        Tweet tweet = tweetDAO.getTweet(ownerMale);
        logger.debug("found tweet: " + tweet);
        return tweet;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Tweet getTweet(boolean ownerMale, Date minDate, Date maxDate) throws SQLException, NoSuchEntityException {
        logger.debug("begin search tweet with parameters: " +
                "owner male = " + ownerMale +
                "min date = " + TimeWizard.formatDateWithTime(minDate.getTime()) +
                "max date = " + TimeWizard.formatDateWithTime(maxDate.getTime()));
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
