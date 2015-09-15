package com.mr_faton.core.manager;

import com.mr_faton.core.dao.TwitterUserManagerDAO;
import com.mr_faton.core.table.TwitterUser;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class TwitterUserManager {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.manager.TwitterUserManager");
    private List<TwitterUser> userList;
    private TwitterUserManagerDAO twitterUserManagerDAO;
    private static final String ON = "ON";
    private static final String OFF = "OFF";

    public void loadUserList() throws SQLException {
        logger.debug("load twitter user list");
        userList = twitterUserManagerDAO.getTwitterUserList();
    }

    public void saveUserList() throws SQLException {
        logger.debug("save twitter user list");
        twitterUserManagerDAO.saveTwitterUserList(userList);
    }

    public List<List<String>> getStatistic() {
        return null;
    }

    public void addUser(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {

    }

    public List<TwitterUser> getUserList() {
        logger.debug("get twitter user list");
        return userList;
    }

    public TwitterUser getUserForTweet() {
        logger.debug("get user for tweet");
        TwitterUser resultUser = null;
        long minNextTweet = Long.MAX_VALUE;
        for (TwitterUser user : userList) {
            if (user.getStatus() && user.getTweetStatus()){
                if (minNextTweet >= user.getNextTweet()) {
                    minNextTweet = user.getNextTweet();
                    resultUser = user;
                }
            }
        }
        if (resultUser != null) {
            logger.debug("the user is " + resultUser.getName());
        } else {
            logger.debug("no user for tweet");
        }

        return resultUser;
    }
}
