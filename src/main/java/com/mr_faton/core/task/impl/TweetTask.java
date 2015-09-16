package com.mr_faton.core.task.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.TwitterMessageDAO;
import com.mr_faton.core.manager.TwitterUserManager;
import com.mr_faton.core.table.TwitterMessage;
import com.mr_faton.core.table.TwitterUser;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 16.09.2015.
 */

/*TODO finish this*/
public class TweetTask implements Task {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.TweetTask");
    private final TwitterUserManager twitterUserManager;
    private final TwitterMessageDAO twitterMessagesDAO;
    private final TwitterAPI twitterAPI;
    private boolean status = true;
    private TwitterUser user = null;


    public TweetTask(
            TwitterUserManager twitterUserManager,
            TwitterMessageDAO twitterMessagesDAO,
            TwitterAPI twitterAPI) {
        logger.debug("constructor");
        this.twitterUserManager = twitterUserManager;
        this.twitterMessagesDAO = twitterMessagesDAO;
        this.twitterAPI = twitterAPI;
    }

    @Override
    public boolean getStatus() {
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public long getTime() {
        logger.debug("get time");
        if (user == null) {
            logger.debug("no user for tweet, return Long.MAX_VALUE");
            return Long.MAX_VALUE;
        }

        logger.debug("user time is " + user.getNextTweet());
        return user.getNextTweet();
    }

    @Override
    public void setTime() {
        logger.debug("set time for next tweet");
        user.setNextTweet(evalNextTweetTime(user));
    }

    @Override
    public void update() {
        logger.debug("get user for tne next tweet");
        user = twitterUserManager.getUserForTweet();
    }

    @Override
    public void setDailyParams() {
        logger.debug("set daily params for all enabled tweet users");
        List<TwitterUser> userList = twitterUserManager.getUserList();
        for (TwitterUser twitterUser : userList) {
            if (twitterUser.getTweetStatus() && twitterUser.getLastUpdateDay() != TimeWizard.getCurDay()) {
                twitterUser.setCurTweets(0);
                twitterUser.setMaxTweets(evalMaxTweets(twitterUser));
                twitterUser.setNextTweet(evalNextTweetTime(twitterUser));
                twitterUser.setLastUpdateDay(TimeWizard.getCurDay());
            }
        }
    }

    /*TODO specially finish this*/
    @Override
    public void run() {
        logger.info("post tweet");
        try {
            int currentTweets = user.getCurTweets();
            int maxTweets = user.getMaxTweets();
            if (currentTweets == maxTweets) {
                logger.debug("done work, because currentTweets == maxTweets");
                return;
            }

            TwitterMessage twitterMessage = twitterMessagesDAO.getTweet(user.isMale());

            twitterAPI.postTweet(user, twitterMessage.getMessage());
            currentTweets++;
            user.setCurTweets(currentTweets);
            twitterMessagesDAO.updatePosted(twitterMessage);
        } catch (SQLException e) {
            logger.warn("exception while loading tweet fom DB", e);
        } catch (TwitterException e) {
            logger.warn("exception while posting tweet", e);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }



    private int evalMaxTweets(TwitterUser twitterUser) {
        logger.debug("evaluate max tweets for user " + twitterUser.getName());
//        Date creationDate = twitterUser.getCreationDate();
//        int min = 2;
//        int max = 6;
//        int rnd = RandomGenerator.getNumber(min, max);
//        return rnd;
        return 3;
    }

    private long evalNextTweetTime(TwitterUser twitterUser) {
        logger.debug("evaluate next tweet time for user " + twitterUser.getName());
        Date creationDate = twitterUser.getCreationDate();
        int curTweets = twitterUser.getCurTweets();
        int maxTweets = twitterUser.getMaxTweets();

        if (curTweets == maxTweets) {
            return Long.MAX_VALUE;
        }

        int min = 10;
        int max = 90;
        int rnd = RandomGenerator.getNumber(min, max);
        long nextTime = rnd * 1000 + System.currentTimeMillis();
        return nextTime;
    }
}
