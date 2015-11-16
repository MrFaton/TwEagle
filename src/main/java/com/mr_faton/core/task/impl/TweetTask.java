package com.mr_faton.core.task.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.LimitExhaustedException;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.table.PostedMessage;
import com.mr_faton.core.table.TweetUser;
import com.mr_faton.core.table.User;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.task.util.MessageUpdateStrategy;
import com.mr_faton.core.util.RandomGenerator;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 * @version 1.5
 */
public class TweetTask implements Task {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.TweetTask");

    @Autowired
    private TwitterAPI twitterAPI;
    @Autowired
    private TweetUserDAO tweetUserDAO;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private PostedMessageDAO postedMessageDAO;
    @Autowired
    private UserDAO userDAO;

    private boolean status = true;
    private TweetUser tweetUser = null;
    private Message message = null;
    private long postedMessageId = 0;


    @Override
    public boolean getStatus() {
        logger.debug("status is " + status);
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        logger.info("status changed to " + status);
        this.status = status;
    }

    @Override
    public long getTime() {
        logger.debug("get time");
        if (tweetUser == null) {
            logger.debug("tweetUser == null");
            return Long.MAX_VALUE;
        }
        logger.debug("next tweet planed on " + TimeWizard.formatDateWithTime(tweetUser.getNextTweet().getTime()));
        return tweetUser.getNextTweet().getTime();
    }

    @Override
    public void setNextTime() {
        logger.debug("set time");
        int currentTweets = tweetUser.getCurTweets();
        int maxTweets = tweetUser.getMaxTweets();
        currentTweets ++;

        if (currentTweets == maxTweets) tweetUser.setNextTweet(new Date(Long.MAX_VALUE));

//        tweetUser.setNextTweet(evalNextTweetTime(tweetUser));
        logger.debug("next tweet has planed on " + TimeWizard.formatDateWithTime(tweetUser.getNextTweet().getTime()));
    }

    @Override
    public void update() throws SQLException {
        logger.debug("update task");
        message = null;
        postedMessageId = 0;
        try {
            tweetUser = tweetUserDAO.getUserForTweet();
        } catch (SQLException e) {
            logger.warn("exception during load tweetUser for tweet from db", e);
            tweetUser = null;
            status = false;
            throw e;
        } catch (NoSuchEntityException e) {
            logger.debug("no tweetUser in db for tweet");
            tweetUser = null;
        }
    }

    /*TODO not finished*/
    @Override
    public void save() throws SQLException {
        logger.debug("save used tweetUser into db");



        PostedMessage postedMessage = createPostedMessage(postedMessageId, message);
        postedMessageDAO.save(postedMessage);

        try {
            tweetUserDAO.update(tweetUser);
        } catch (SQLException e) {
            logger.warn("exception during saving updated tweetUser in db");
            throw e;
        }
    }

    @Override
    public void setDailyParams() throws SQLException {
        logger.debug("set daily params");
        try {
            List<TweetUser> tweetUserList = tweetUserDAO.getUserList();
            for (TweetUser tweetUser : tweetUserList) {
                int curDay = TimeWizard.getCurDay();
                if (curDay != tweetUser.getLastUpdateDay() && tweetUser.isTweet()) {
                    tweetUser.setCurTweets(0);
                    tweetUser.setMaxTweets(evalMaxTweets(tweetUser));
//                    tweetUser.setNextTweet(evalNextTweetTime(tweetUser));
                    tweetUser.setLastUpdateDay(curDay);
                }
            }
            tweetUserDAO.update(tweetUserList);
        } catch (SQLException e) {
            logger.warn("exception while updating tweet tweetUser list", e);
            throw e;
        } catch (NoSuchEntityException e) {
            logger.debug("no tweet users in db", e);
        }
    }

    @Override
    public void execute() {
        logger.info("post tweet");

        try {


            User user = userDAO.getUserByName(tweetUser.getName());

//            try {
//                message = messageDAO.getTweetFirstTry(user.isMale());
//            } catch (NoSuchEntityException entityEx1) {
//                try {
//                    message = messageDAO.getTweetSecondTry(user.isMale());
//                } catch (NoSuchEntityException entityEx2) {
//                    try {
//                        message = messageDAO.getTweetThirdTry(user.isMale());
//                    } catch (NoSuchEntityException entityEx3) {
//                        message = messageDAO.getAnyTweet(user.isMale());
//                    }
//                }
//            }
            postedMessageId = twitterAPI.postTweet(tweetUser.getName(), message.getMessage());

            logger.info("tweet successful posted for " + tweetUser.getName() + " with id: " + postedMessageId + " " +
                    "and text: " + message.getMessage());


            int currentTweets = tweetUser.getCurTweets();
            currentTweets++;
            tweetUser.setCurTweets(currentTweets);


        } catch (LimitExhaustedException limitExhaustedException) {
            logger.warn(limitExhaustedException.getMessage());
        } catch (TwitterException e) {
            logger.warn("exception while posting tweet", e);
            tweetUser.setTweet(false);
        } catch (NoSuchEntityException e) {
            logger.warn("exception", e);
        } catch (SQLException e) {
            logger.warn("some sql exception", e);
            tweetUser.setTweet(false);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }


    private int evalMaxTweets(TweetUser tweetUser) throws SQLException, NoSuchEntityException {
        logger.debug("evaluate max tweets for tweetUser " + tweetUser.getName());
        User user = userDAO.getUserByName(tweetUser.getName());
        Calendar creationDate = Calendar.getInstance();
        creationDate.setTime(user.getCreationDate());

        int lifeMonth = TimeWizard.monthDiff(creationDate);
        lifeMonth++;
        if (lifeMonth >= 12) lifeMonth = 12;
        int maxPost = lifeMonth * 4;
        return RandomGenerator.getNumberFromZeroToRequirement(maxPost);

//        switch (lifeMonth) {
//            case 0: {
//                return RandomGenerator.getNumberFromZeroToRequirement(4);
//            }
//            case 1: {
//                return RandomGenerator.getNumberFromZeroToRequirement(8);
//            }
//            case 2: {
//                return RandomGenerator.getNumberFromZeroToRequirement(12);
//            }
//            case 3: {
//                return RandomGenerator.getNumberFromZeroToRequirement(16);
//            }
//            case 4: {
//                return RandomGenerator.getNumberFromZeroToRequirement(20);
//            }
//            case 5: {
//                return RandomGenerator.getNumberFromZeroToRequirement(24);
//            }
//            case 6: {
//                return RandomGenerator.getNumberFromZeroToRequirement(28);
//            }
//            case 7: {
//                return RandomGenerator.getNumberFromZeroToRequirement(32);
//            }
//            case 8: {
//                return RandomGenerator.getNumberFromZeroToRequirement(36);
//            }
//            case 9: {
//                return RandomGenerator.getNumberFromZeroToRequirement(40);
//            }
//            case 10: {
//                return RandomGenerator.getNumberFromZeroToRequirement(44);
//            }
//            case 11: {
//                return RandomGenerator.getNumberFromZeroToRequirement(48);
//            }
//            default: {
//                return RandomGenerator.getNumberFromZeroToRequirement(52);
//            }
//        }


//        return 3;
    }


/*TODO debug*/
    private long evalNextTweetTime(TweetUser tweetUser) {
        logger.debug("evaluate next tweet time for tweetUser " + tweetUser.getName());
        int curTweets = tweetUser.getCurTweets();
        int maxTweets = tweetUser.getMaxTweets();

        if (curTweets == 0) return MessageUpdateStrategy.getStartTimeForUser(tweetUser.getName());

        int tempCurTweets =  curTweets + 1;

        if (tempCurTweets == maxTweets) {
            return Long.MAX_VALUE;
        }

        final int maxDistancePercent = 50;

        long startUserTime = MessageUpdateStrategy.getStartTimeForUser(tweetUser.getName());
        long stopUserTime = MessageUpdateStrategy.getStopTimeForUser(tweetUser.getName());
        long fixedRange = (stopUserTime - startUserTime) / tweetUser.getMaxTweets();
        int fixedRangePercent = RandomGenerator.getNumberFromZeroToRequirement(maxDistancePercent);
        long floatRange = (fixedRange * fixedRangePercent / 100);

        long fixedNextTimePosting = (fixedRange * curTweets) + startUserTime;
        boolean plus = RandomGenerator.getRandomBoolean();

        return plus ? (fixedNextTimePosting + floatRange):(fixedNextTimePosting - floatRange);





//        int min = 10;
//        int max = 90;
//        int rnd = RandomGenerator.getNumber(min, max);
//        return rnd * 1000 + System.currentTimeMillis();
    }

    private PostedMessage createPostedMessage(long messageId, Message message) {
        PostedMessage postedMessage = new PostedMessage();
        postedMessage.setMessage(message.getMessage());
        postedMessage.setTwitterId(messageId);
        postedMessage.setOwner(message.getOwner());
        postedMessage.setRecipient(null);
        Date postedDate = new Date(System.currentTimeMillis());
        postedMessage.setPostedDate(postedDate);

        return postedMessage;
    }
}
