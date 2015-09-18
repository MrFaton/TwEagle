package com.mr_faton.core.table;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public class TweetUser {
    private String name;
    private boolean tweet;
    private int curTweets;
    private int maxTweets;
    private long nextTweet;
    private int lastUpdateDay;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isTweet() {
        return tweet;
    }
    public void setTweet(boolean tweet) {
        this.tweet = tweet;
    }

    public int getCurTweets() {
        return curTweets;
    }
    public void setCurTweets(int curTweets) {
        this.curTweets = curTweets;
    }

    public int getMaxTweets() {
        return maxTweets;
    }
    public void setMaxTweets(int maxTweets) {
        this.maxTweets = maxTweets;
    }

    public long getNextTweet() {
        return nextTweet;
    }
    public void setNextTweet(long nextTweet) {
        this.nextTweet = nextTweet;
    }

    public int getLastUpdateDay() {
        return lastUpdateDay;
    }
    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }
}
