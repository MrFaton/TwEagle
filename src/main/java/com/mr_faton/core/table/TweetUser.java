package com.mr_faton.core.table;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 */
public class TweetUser {
    private int id;
    private String name;
    private boolean tweet;
    private int curTweets;
    private int maxTweets;
    private Date nextTweet;
    private int lastUpdateDay;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public Date getNextTweet() {
        return nextTweet;
    }
    public void setNextTweet(Date nextTweet) {
        this.nextTweet = nextTweet;
    }

    public int getLastUpdateDay() {
        return lastUpdateDay;
    }
    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetUser tweetUser = (TweetUser) o;

        if (id != tweetUser.id) return false;
        if (tweet != tweetUser.tweet) return false;
        if (curTweets != tweetUser.curTweets) return false;
        if (maxTweets != tweetUser.maxTweets) return false;
        if (lastUpdateDay != tweetUser.lastUpdateDay) return false;
        if (!name.equals(tweetUser.name)) return false;
        return !(nextTweet != null ? !nextTweet.equals(tweetUser.nextTweet) : tweetUser.nextTweet != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (tweet ? 1 : 0);
        result = 31 * result + curTweets;
        result = 31 * result + maxTweets;
        result = 31 * result + (nextTweet != null ? nextTweet.hashCode() : 0);
        result = 31 * result + lastUpdateDay;
        return result;
    }
}
