package com.mr_faton.core.dao;

import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.table.TwitterUser;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TwitterUserManagerDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.TwitterUserManagerDAO");
    private static final String TABLE = "tweagle.twitter_users";
    private final DBConnectionPool connectionPool;

    public TwitterUserManagerDAO(DBConnectionPool connectionPool) {
        logger.debug("constructor");
        this.connectionPool = connectionPool;
    }

    public List<TwitterUser> getTwitterUserList() throws SQLException {
        logger.debug("get twitter user list from DB");
        final String SQL = "" +
                "SELECT " +

                "ac_name, " +
                "ac_password, " +
                "ac_email, " +
                "ac_status, " +
                "male, " +
                "creation_date, " +
                "last_update_day, " +

                "messages, " +
                "following, " +
                "followers, " +

                "is_tweet, " +
                "cur_tweet, " +
                "max_tweet, " +
                "next_tweet, " +

                "is_mention, " +
                "cur_mention, " +
                "max_mention, " +
                "next_mention, " +

                "is_retweet, " +
                "cur_retweet, " +
                "max_retweet, " +
                "next_retweet, " +

                "is_dm, " +
                "cur_dm, " +
                "max_dm, next_dm, " +

                "is_follow, " +
                "cur_follow, " +
                "max_follow, " +
                "next_follow, " +

                "is_followback, " +
                "cur_followback, " +
                "max_followback, " +
                "next_followback, " +

                "is_unfollow, " +
                "cur_unfollow, " +
                "max_unfollow, " +
                "next_unfollow, " +

                "is_change_text_status, " +
                "next_change_text_status, " +

                "consumer_key, " +
                "consumer_secret, " +
                "access_token, " +
                "access_token_secret " +

                "FROM " + TABLE + ";";

        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        List<TwitterUser> twitterUserList = new ArrayList<>();

        while (resultSet.next()){
            TwitterUser user = new TwitterUser();

            user.setName(resultSet.getString("ac_name"));
            user.setPassword(resultSet.getString("ac_password"));
            user.setEmail(resultSet.getString("ac_email"));
            user.setStatus(resultSet.getBoolean("ac_status"));
            user.setMale(resultSet.getBoolean("male"));
            user.setCreationDate(resultSet.getDate("creation_date"));
            user.setLastUpdateDay(resultSet.getInt("last_update_day"));

            user.setMessages(resultSet.getInt("messages"));
            user.setFollowing(resultSet.getInt("following"));
            user.setFollowers(resultSet.getInt("followers"));

            user.setTweetStatus(resultSet.getBoolean("is_tweet"));
            user.setCurTweets(resultSet.getInt("cur_tweet"));
            user.setMaxTweets(resultSet.getInt("max_tweet"));
            user.setNextTweet(resultSet.getLong("next_tweet"));

            user.setMentionsStatus(resultSet.getBoolean("is_mention"));
            user.setCurMentions(resultSet.getInt("cur_mention"));
            user.setMaxMentions(resultSet.getInt("max_mention"));
            user.setNextMention(resultSet.getLong("next_mention"));

            user.setRetweetStatus(resultSet.getBoolean("is_retweet"));
            user.setCurRetweets(resultSet.getInt("cur_retweet"));
            user.setMaxRetweets(resultSet.getInt("max_retweet"));
            user.setNextRetweet(resultSet.getLong("next_retweet"));

            user.setDmStatus(resultSet.getBoolean("is_dm"));
            user.setCurDms(resultSet.getInt("cur_dm"));
            user.setMaxDms(resultSet.getInt("max_dm"));
            user.setNextDm(resultSet.getLong("next_dm"));

            user.setFollowStatus(resultSet.getBoolean("is_follow"));
            user.setCurFollows(resultSet.getInt("cur_follow"));
            user.setMaxFollows(resultSet.getInt("max_follow"));
            user.setNextFollow(resultSet.getLong("next_follow"));

            user.setFollowBackStatus(resultSet.getBoolean("is_followback"));
            user.setCurFollowBacks(resultSet.getInt("cur_followback"));
            user.setMaxFollowBacks(resultSet.getInt("max_followback"));
            user.setNextFollowBack(resultSet.getLong("next_followback"));

            user.setUnFollowStatus(resultSet.getBoolean("is_unfollow"));
            user.setCurUnFollows(resultSet.getInt("cur_unfollow"));
            user.setMaxUnFollows(resultSet.getInt("max_unfollow"));
            user.setNextUnFollow(resultSet.getLong("next_unfollow"));

            user.setChangeTextStatus_Status(resultSet.getBoolean("is_change_text_status"));
            user.setNextChangeStatus(resultSet.getLong("next_change_text_status"));

            user.setConsumerKey(resultSet.getString("consumer_key"));
            user.setConsumerSecret(resultSet.getString("consumer_secret"));
            user.setAccessToken(resultSet.getString("access_token"));
            user.setAccessTokenSecret(resultSet.getString("access_token_secret"));

            twitterUserList.add(user);
        }

        logger.debug("found " + twitterUserList.size() + " users in DB");

        resultSet.close();
        statement.close();
        connection.close();

        return twitterUserList;
    }

    public void saveTwitterUserList(List<TwitterUser> twitterUserList) throws SQLException {
        logger.debug("save twitter user list, size = " + twitterUserList.size());
        final String SQL = "" +
                "UPDATE " + TABLE + " SET " +
                "ac_status = ?, " +//1
                "messages = ?, " +//2
                "following = ?, " +//3
                "followers = ?, " +//4

                "is_tweet = ?, " +//5
                "cur_tweet = ?, " +//6
                "max_tweet = ?, " +//7
                "next_tweet = ?, " +//8

                "is_mention = ?, " +//9
                "cur_mention = ?, " +//10
                "max_mention = ?, " +//11
                "next_mention = ?, " +//12

                "is_retweet = ?, " +//13
                "cur_retweet = ?, " +//14
                "max_retweet = ?, " +//15
                "next_retweet = ?, " +//16

                "is_dm = ?, " +//17
                "cur_dm = ?, " +//18
                "max_dm = ?, " +//19
                "next_dm = ?, " +//20

                "is_follow = ?, " +//21
                "cur_follow = ?, " +//22
                "max_follow = ?, " +//23
                "next_follow = ?, " +//24

                "is_followback = ?, " +//25
                "cur_followback = ?, " +//26
                "max_followback = ?, " +//27
                "next_followback = ?, " +//28

                "is_unfollow = ?, " +//29
                "cur_unfollow = ?, " +//30
                "max_unfollow = ?, " +//31
                "next_unfollow = ?, " +//32

                "is_change_text_status = ?, " +//33
                "next_change_text_status = ?, " +//34

                "last_update_day = ? " + //35

                "WHERE ac_name = ?;";//36

        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        for (TwitterUser user : twitterUserList) {

            preparedStatement.setBoolean(1, user.getStatus());
            preparedStatement.setInt(2, user.getMessages());
            preparedStatement.setInt(3, user.getFollowing());
            preparedStatement.setInt(4, user.getFollowers());

            preparedStatement.setBoolean(5, user.getTweetStatus());
            preparedStatement.setInt(6, user.getCurTweets());
            preparedStatement.setInt(7, user.getMaxTweets());
            preparedStatement.setLong(8, user.getNextTweet());

            preparedStatement.setBoolean(9, user.getMentionsStatus());
            preparedStatement.setInt(10, user.getCurMentions());
            preparedStatement.setInt(11, user.getMaxMentions());
            preparedStatement.setLong(12, user.getNextMention());

            preparedStatement.setBoolean(13, user.getRetweetStatus());
            preparedStatement.setInt(14, user.getCurRetweets());
            preparedStatement.setInt(15, user.getMaxRetweets());
            preparedStatement.setLong(16, user.getNextRetweet());

            preparedStatement.setBoolean(17, user.getDmStatus());
            preparedStatement.setInt(18, user.getCurDms());
            preparedStatement.setInt(19, user.getMaxDms());
            preparedStatement.setLong(20, user.getNextDm());

            preparedStatement.setBoolean(21, user.getFollowStatus());
            preparedStatement.setInt(22, user.getCurFollows());
            preparedStatement.setInt(23, user.getMaxFollows());
            preparedStatement.setLong(24, user.getNextFollow());

            preparedStatement.setBoolean(25, user.getFollowBackStatus());
            preparedStatement.setInt(26, user.getCurFollowBacks());
            preparedStatement.setInt(27, user.getMaxFollowBacks());
            preparedStatement.setLong(28, user.getNextFollowBack());

            preparedStatement.setBoolean(29, user.getUnFollowStatus());
            preparedStatement.setInt(30, user.getCurUnFollows());
            preparedStatement.setInt(31, user.getMaxUnFollows());
            preparedStatement.setLong(32, user.getNextUnFollow());

            preparedStatement.setBoolean(33, user.getChangeTextStatus_Status());
            preparedStatement.setLong(34, user.getNextChangeStatus());

            preparedStatement.setInt(35, user.getLastUpdateDay());

            preparedStatement.setString(36, user.getName());

            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
        preparedStatement.close();
        connection.close();

    }

}
