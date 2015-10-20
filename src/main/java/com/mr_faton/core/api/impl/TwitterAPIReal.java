package com.mr_faton.core.api.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.LimitExhaustedException;
import com.mr_faton.core.exception.NoSuchEntityException;
import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TwitterAPIReal implements TwitterAPI{
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.api.impl.TwitterAPIReal");
    private final String USER_LANG = "ru";
    private final Date USER_LAST_POST_DATE;
    private static final Map<String, Twitter> TWITTER_STORAGE = new ConcurrentHashMap<>();
    private final UserDAO userDAO;
    private static final String APP_LIMIT = "/application/rate_limit_status";


    public TwitterAPIReal(UserDAO userDAO) {
        logger.debug("constructor");
        this.userDAO = userDAO;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -3);
        USER_LAST_POST_DATE = new Date(calendar.getTimeInMillis());
    }

    @Override
    public long postTweet(String userName, String tweet) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("post tweet for user " + userName + " with text " + tweet);
        canWork(userName);
        Twitter twitter = getTwitter(userName);
        Status status = twitter.updateStatus(tweet);
        long tweetId = status.getId();
        logger.debug("tweet posted with id " + tweetId);
        return tweetId;
    }

    @Override
    public void deleteTweetById(String userName, long id) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("delete tweet for user " + userName + " by id " + id);
        canWork(userName);
        Twitter twitter = getTwitter(userName);
        twitter.destroyStatus(id);
    }

    @Override
    public void deleteLastTweet(String userName) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("delete last tweet fo user " + userName);
        canWork(userName);
        Twitter twitter = getTwitter(userName);
        ResponseList<Status> homeTimeline = twitter.getUserTimeline(new Paging(1, 1));
        Status lastStatus = homeTimeline.get(0);
        String text = lastStatus.getText();
        long id = lastStatus.getId();
        twitter.destroyStatus(id);
        logger.debug("tweet: " + text + ", id: " + id + " was deleted");
    }

    @Override
    public ResponseList<Status> getUserTimeLine(
            String donorUserName,
            Paging paging,
            String sourceUserName) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("get messages from donor " + donorUserName + ", page " + paging.getPage());
        canWork(sourceUserName);
        Twitter twitter = getTwitter(sourceUserName);
        try {
            return twitter.getUserTimeline(donorUserName, paging);
        } catch (TwitterException twitterEx) {
            int statusCode = twitterEx.getStatusCode();
            switch (statusCode) {
                case 401: {
                    User user = twitter.showUser(donorUserName);
                    if (user.isProtected()) {
                        logger.debug("twitter user " + donorUserName + " is protected for parse messages");
                        logger.warn(donorUserName + ", ");
                        return createDefaultResponseList();
                    } else {
                        throw twitterEx;
                    }
                }
                case 404: {
                    logger.debug("twitter user " + donorUserName + " not found");
                    logger.warn(donorUserName + ", ");
                    return createDefaultResponseList();
                }
                default: {
                    throw twitterEx;
                }
            }
        }

    }

    private int getAppLimit(String userName) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("get application limit status for user " + userName);
        Twitter twitter = getTwitter(userName);
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
        int remainLimit = rateLimitStatus.get(APP_LIMIT).getRemaining();
        logger.info(remainLimit + " twitter queries left");
        return remainLimit;
    }

    private int getSecondsUntilResetAppLimit(String userName) throws TwitterException, SQLException, NoSuchEntityException {
        logger.debug("get seconds until reset application limit for user " + userName);
        Twitter twitter = getTwitter(userName);
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
        int secondsUntilReset = rateLimitStatus.get(APP_LIMIT).getSecondsUntilReset();
        logger.debug(secondsUntilReset + " left to reset application limit");
        return secondsUntilReset;
    }

    private Twitter getTwitter(String userName) throws SQLException, TwitterException, NoSuchEntityException {
        logger.debug("get twitter object for user " + userName);
        if (TWITTER_STORAGE.containsKey(userName)) {
            return TWITTER_STORAGE.get(userName);
        }
        logger.debug("no twitter by name " + userName + ", must create it");

        com.mr_faton.core.table.User user = userDAO.getUserByName(userName);

        String consumerKey = user.getConsumerKey();
        String consumerSecret = user.getConsumerSecret();
        String accessToken = user.getAccessToken();
        String accessTokenSecret = user.getAccessTokenSecret();

        System.out.println("consumer key=" + consumerKey);
        System.out.println("consumer secret=" + consumerSecret);
        System.out.println("access token=" + accessToken);
        System.out.println("access token secret=" + accessTokenSecret);

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        TWITTER_STORAGE.put(userName, twitter);
        return twitter;
    }

    private void canWork(String userName) throws NoSuchEntityException, SQLException, TwitterException {
        int appLimit = getAppLimit(userName);
        if (appLimit == 0) {
            logger.warn("application limit was exhausted");
            throw new LimitExhaustedException("application limit was exhausted");
        }
    }

    private ResponseList<Status> createDefaultResponseList() {
        return new ResponseList<Status>() {
            @Override
            public RateLimitStatus getRateLimitStatus() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Status> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Status status) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Status> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Status> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Status get(int index) {
                return null;
            }

            @Override
            public Status set(int index, Status element) {
                return null;
            }

            @Override
            public void add(int index, Status element) {

            }

            @Override
            public Status remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Status> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Status> listIterator(int index) {
                return null;
            }

            @Override
            public List<Status> subList(int fromIndex, int toIndex) {
                return null;
            }

            @Override
            public int getAccessLevel() {
                return 0;
            }
        };
    }

    private boolean twitterUserValidator(Twitter twitter, String donorName) throws TwitterException {
        try {
            User user = twitter.showUser(donorName);
            if (user.isProtected()) {
                logger.debug("bad donorUser " + donorName + ", because its protected");
                logger.warn(donorName + ", ");
                return false;
            }
            if (!USER_LANG.equals(user.getLang())) {
                logger.debug("bad donorUser " + donorName + ", because its lang = " + user.getLang());
                logger.warn(donorName + ", ");
                return false;
            }
            if (USER_LAST_POST_DATE.after(user.getStatus().getCreatedAt())) {
                logger.debug("bad donorUser " + donorName + ", because its last status update in " +
                        String.format("%td.%<tm.%<tY", user.getStatus().getCreatedAt()));
                logger.warn(donorName + ", ");
                return false;
            }
            return true;
        } catch (TwitterException e) {
            int statusCode = e.getStatusCode();
            switch (statusCode) {
                case 404: {
                    logger.debug("bad donorUser " + donorName + ", because its not found");
                    logger.warn(donorName + ", ");
                    return false;
                }
                default: {
                    throw e;
                }
            }
        }
    }
}
