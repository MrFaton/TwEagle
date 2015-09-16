package com.mr_faton.core.api;

import com.mr_faton.core.table.TwitterUser;
import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TwitterAPI{
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.api.TwitterAPI");
    private static final Map<String, Twitter> TWITTER_STORAGE = new ConcurrentHashMap<>();
    private static final String APP_LIMIT = "/application/rate_limit_status";

    public TwitterAPI() {
        logger.info("constructor");
    }

    public int getAppLimit(TwitterUser user) throws TwitterException {
        logger.debug("get application limit status for user " + user.getName());
        Twitter twitter = getTwitter(user);
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
        int remainLimit = rateLimitStatus.get(APP_LIMIT).getRemaining();
        logger.debug(remainLimit + " twitter queries left");
        return remainLimit;
    }

    public int getSecondsUntilResetAppLimit(TwitterUser user) throws TwitterException {
        logger.debug("get seconds until reset application limit for user " + user.getName());
        Twitter twitter = getTwitter(user);
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
        int secondsUntilReset = rateLimitStatus.get(APP_LIMIT).getSecondsUntilReset();
        logger.debug(secondsUntilReset + " left to reset application limit");
        return secondsUntilReset;
    }

    public long postTweet(TwitterUser user, String tweet) throws TwitterException {
        logger.debug("post tweet for user " + user.getName());
        Twitter twitter = getTwitter(user);
        Status status = twitter.updateStatus(tweet);
        long tweetId = status.getId();
        logger.debug("tweet posted with id " + tweetId);
        return tweetId;
    }

    public void deleteTweetById(TwitterUser user, long id) throws TwitterException {
        logger.debug("delete tweet for user " + user.getName() + " by id " + id);
        Twitter twitter = getTwitter(user);
        twitter.destroyStatus(id);
    }

    public void deleteLastTweet(TwitterUser user) throws TwitterException {
        logger.debug("delete last tweet fo user " + user.getName());
        Twitter twitter = getTwitter(user);
        ResponseList<Status> homeTimeline = twitter.getUserTimeline(new Paging(1, 1));
        Status lastStatus = homeTimeline.get(0);
        String text = lastStatus.getText();
        long id = lastStatus.getId();
        twitter.destroyStatus(id);
        logger.debug("tweet: " + text + ", id: " + id + " was deleted");
    }



    private Twitter getTwitter(TwitterUser user) {
        logger.debug("get twitter object for user " + user.getName());
        String userName = user.getName();
        if (TWITTER_STORAGE.containsKey(userName)) {
            return TWITTER_STORAGE.get(userName);
        }
        logger.debug("no twitter by name " + userName + ", must create it");
        String consumerKey = user.getConsumerKey();
        String consumerSecret = user.getConsumerSecret();
        String accessToken = user.getAccessToken();
        String accessTokenSecret = user.getAccessTokenSecret();

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        TWITTER_STORAGE.put(userName, twitter);
        return twitter;
    }
}
