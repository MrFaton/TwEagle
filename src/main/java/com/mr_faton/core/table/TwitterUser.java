package com.mr_faton.core.table;

import java.util.Date;

public class TwitterUser {
    private boolean status;
    private String name;
    private String password;
    private String email;
    private boolean male;
    private Date creationDate;
    private int lastUpdateDay;

    private int messages;
    private int following;
    private int followers;

    private boolean tweetStatus;
    private int curTweets;
    private int maxTweets;
    private long nextTweet;

    private boolean mentionsStatus;
    private int curMentions;
    private int maxMentions;
    private long nextMention;

    private boolean retweetStatus;
    private int curRetweets;
    private int maxRetweets;
    private long nextRetweet;

    private boolean dmStatus;
    private int curDms;
    private int maxDms;
    private long nextDm;

    private boolean followStatus;
    private int curFollows;
    private int maxFollows;
    private long nextFollow;

    private boolean followBackStatus;
    private int curFollowBacks;
    private int maxFollowBacks;
    private long nextFollowBack;

    private boolean unFollowStatus;
    private int curUnFollows;
    private int maxUnFollows;
    private long nextUnFollow;

    private boolean changeTextStatus_Status;
    private long nextChangeStatus;

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;


    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return male;
    }
    public void setMale(boolean male) {
        this.male = male;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getLastUpdateDay() {
        return lastUpdateDay;
    }
    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }




    public int getMessages() {
        return messages;
    }
    public void setMessages(int messages) {
        this.messages = messages;
    }

    public int getFollowing() {
        return following;
    }
    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }
    public void setFollowers(int followers) {
        this.followers = followers;
    }




    public boolean getTweetStatus() {
        return tweetStatus;
    }
    public void setTweetStatus(boolean tweetStatus) {
        this.tweetStatus = tweetStatus;
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

    public boolean getMentionsStatus() {
        return mentionsStatus;
    }
    public void setMentionsStatus(boolean mentionsStatus) {
        this.mentionsStatus = mentionsStatus;
    }
    public int getCurMentions() {
        return curMentions;
    }
    public void setCurMentions(int curMentions) {
        this.curMentions = curMentions;
    }
    public int getMaxMentions() {
        return maxMentions;
    }
    public void setMaxMentions(int maxMentions) {
        this.maxMentions = maxMentions;
    }
    public long getNextMention() {
        return nextMention;
    }
    public void setNextMention(long nextMention) {
        this.nextMention = nextMention;
    }

    public boolean getRetweetStatus() {
        return retweetStatus;
    }
    public void setRetweetStatus(boolean retweetStatus) {
        this.retweetStatus = retweetStatus;
    }
    public int getCurRetweets() {
        return curRetweets;
    }
    public void setCurRetweets(int curRetweets) {
        this.curRetweets = curRetweets;
    }
    public int getMaxRetweets() {
        return maxRetweets;
    }
    public void setMaxRetweets(int maxRetweets) {
        this.maxRetweets = maxRetweets;
    }
    public long getNextRetweet() {
        return nextRetweet;
    }
    public void setNextRetweet(long nextRetweet) {
        this.nextRetweet = nextRetweet;
    }

    public boolean getDmStatus() {
        return dmStatus;
    }
    public void setDmStatus(boolean dmStatus) {
        this.dmStatus = dmStatus;
    }
    public int getCurDms() {
        return curDms;
    }
    public void setCurDms(int curDms) {
        this.curDms = curDms;
    }
    public int getMaxDms() {
        return maxDms;
    }
    public void setMaxDms(int maxDms) {
        this.maxDms = maxDms;
    }
    public long getNextDm() {
        return nextDm;
    }
    public void setNextDm(long nextDm) {
        this.nextDm = nextDm;
    }

    public boolean getFollowStatus() {
        return followStatus;
    }
    public void setFollowStatus(boolean followStatus) {
        this.followStatus = followStatus;
    }
    public int getCurFollows() {
        return curFollows;
    }
    public void setCurFollows(int curFollows) {
        this.curFollows = curFollows;
    }
    public int getMaxFollows() {
        return maxFollows;
    }
    public void setMaxFollows(int maxFollows) {
        this.maxFollows = maxFollows;
    }
    public long getNextFollow() {
        return nextFollow;
    }
    public void setNextFollow(long nextFollow) {
        this.nextFollow = nextFollow;
    }

    public boolean getFollowBackStatus() {
        return followBackStatus;
    }
    public void setFollowBackStatus(boolean followBackStatus) {
        this.followBackStatus = followBackStatus;
    }
    public int getCurFollowBacks() {
        return curFollowBacks;
    }
    public void setCurFollowBacks(int curFollowBacks) {
        this.curFollowBacks = curFollowBacks;
    }
    public int getMaxFollowBacks() {
        return maxFollowBacks;
    }
    public void setMaxFollowBacks(int maxFollowBacks) {
        this.maxFollowBacks = maxFollowBacks;
    }
    public long getNextFollowBack() {
        return nextFollowBack;
    }
    public void setNextFollowBack(long nextFollowBack) {
        this.nextFollowBack = nextFollowBack;
    }

    public boolean getUnFollowStatus() {
        return unFollowStatus;
    }
    public void setUnFollowStatus(boolean unFollowStatus) {
        this.unFollowStatus = unFollowStatus;
    }
    public int getCurUnFollows() {
        return curUnFollows;
    }
    public void setCurUnFollows(int curUnFollows) {
        this.curUnFollows = curUnFollows;
    }
    public int getMaxUnFollows() {
        return maxUnFollows;
    }
    public void setMaxUnFollows(int maxUnFollows) {
        this.maxUnFollows = maxUnFollows;
    }
    public long getNextUnFollow() {
        return nextUnFollow;
    }
    public void setNextUnFollow(long nextUnFollow) {
        this.nextUnFollow = nextUnFollow;
    }

    public boolean getChangeTextStatus_Status() {
        return changeTextStatus_Status;
    }
    public void setChangeTextStatus_Status(boolean changeTextStatus_Status) {
        this.changeTextStatus_Status = changeTextStatus_Status;
    }
    public long getNextChangeStatus() {
        return nextChangeStatus;
    }
    public void setNextChangeStatus(long nextChangeStatus) {
        this.nextChangeStatus = nextChangeStatus;
    }

    public String getConsumerKey() {
        return consumerKey;
    }
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }
    public String getConsumerSecret() {
        return consumerSecret;
    }
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }
}
