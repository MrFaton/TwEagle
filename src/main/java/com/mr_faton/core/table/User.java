package com.mr_faton.core.table;

import java.util.Date;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public class User {
    private String name;
    private String password;
    private String email;
    private Date creationDate;
    private boolean male;

    private int messages;
    private int following;
    private int followers;

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;



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

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isMale() {
        return male;
    }
    public void setMale(boolean male) {
        this.male = male;
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
