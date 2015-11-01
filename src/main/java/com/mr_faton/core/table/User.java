package com.mr_faton.core.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 * @version 1.0
 */

@Entity
@Table(schema = "tweagle", name = "users")
public class User {
    @Id
    @Column(length = 25, nullable = false, updatable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean male;

    @Column(name = "creation_date", nullable = false, columnDefinition = "DATE")
    private Date creationDate;


    @Column(nullable = false, columnDefinition = "DEFAULT 0")
    private int messages;

    @Column(nullable = false, columnDefinition = "DEFAULT 0")
    private int following;

    @Column(nullable = false, columnDefinition = "DEFAULT 0")
    private int followers;


    @Column(name = "consumer_key", length = 70, nullable = false)
    private String consumerKey;

    @Column(name = "consumer_secret", length = 70, nullable = false)
    private String consumerSecret;

    @Column(name = "access_token", length = 70, nullable = false)
    private String accessToken;

    @Column(name = "access_token_secret", length = 70, nullable = false)
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
}
