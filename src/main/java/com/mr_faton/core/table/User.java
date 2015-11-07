package com.mr_faton.core.table;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 * @version 1.0
 */
public class User{
    private String name;
    private String password;
    private String email;
    private boolean male;
    private Date creationDate;

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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (male != user.male) return false;
        if (messages != user.messages) return false;
        if (following != user.following) return false;
        if (followers != user.followers) return false;
        if (!name.equals(user.name)) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        if (!creationDate.equals(user.creationDate)) return false;
        if (!consumerKey.equals(user.consumerKey)) return false;
        if (!consumerSecret.equals(user.consumerSecret)) return false;
        if (!accessToken.equals(user.accessToken)) return false;
        return accessTokenSecret.equals(user.accessTokenSecret);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (male ? 1 : 0);
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + messages;
        result = 31 * result + following;
        result = 31 * result + followers;
        result = 31 * result + consumerKey.hashCode();
        result = 31 * result + consumerSecret.hashCode();
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + accessTokenSecret.hashCode();
        return result;
    }
}
