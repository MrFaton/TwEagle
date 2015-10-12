package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.TweetUser;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 28.09.2015
 */
public class TweetUserDAOReal implements TweetUserDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.TweetUserDAOReal");
    private final DataSource dataSource;

    public TweetUserDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public TweetUser getUserForTweet() throws SQLException, NoSuchEntityException {
        logger.debug("get user for posting tweets");
        final String SQL = "" +
                "SELECT * FROM tweagle.tweet_users WHERE " +
                "is_tweet = 1 AND " +
                "cur_tweet < max_tweet AND " +
                "next_tweet = (SELECT MIN(next_tweet) FROM tweagle.tweet_users);";
        Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {

            if (resultSet.next()) {
                TweetUser tweetUser = getTweetUser(resultSet);
                return tweetUser;
            } else {
                throw new NoSuchEntityException();
            }
        }
    }

    @Override
    public List<TweetUser> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get tweet users");
        final String SQL = "SELECT * FROM tweagle.tweet_users;";
        List<TweetUser> tweetUserList = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL)) {

            while (resultSet.next()) {
                tweetUserList.add(getTweetUser(resultSet));
            }
        }
        if (tweetUserList.isEmpty()) throw new NoSuchEntityException();
        return tweetUserList;
    }

    @Override
    public void update(TweetUser user) throws SQLException {
        logger.debug("update tweet user");
        final String SQL = "" +
                "UPDATE tweagle.tweet_users SET " +
                "is_tweet=?, cur_tweet=?, max_tweet=?, next_tweet=?, last_upd=? WHERE name=?;";
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setBoolean(1, user.isTweet());
            preparedStatement.setInt(2, user.getCurTweets());
            preparedStatement.setInt(3, user.getMaxTweets());
            preparedStatement.setTimestamp(4, new Timestamp(user.getNextTweet()));
            preparedStatement.setInt(5, user.getLastUpdateDay());
            preparedStatement.setString(6, user.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<TweetUser> userList) throws SQLException {
        logger.debug("update tweet user list");
        final String SQL = "" +
                "UPDATE tweagle.tweet_users SET " +
                "is_tweet=?, cur_tweet=?, max_tweet=?, next_tweet=?, last_upd=? WHERE name=?;";
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            for (TweetUser tweetUser : userList) {
                preparedStatement.setBoolean(1, tweetUser.isTweet());
                preparedStatement.setInt(2, tweetUser.getCurTweets());
                preparedStatement.setInt(3, tweetUser.getMaxTweets());
                preparedStatement.setTimestamp(4, new Timestamp(tweetUser.getNextTweet()));
                preparedStatement.setInt(5, tweetUser.getLastUpdateDay());
                preparedStatement.setString(6, tweetUser.getName());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    @Override
    public void addUser(String userName) throws SQLException {
        logger.debug("add user to tweet users");
        final String SQL = "" +
                "INSERT INTO tweagle.tweet_users (name) VALUES (?);";
        Connection connection = dataSource. getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        }
    }

    private TweetUser getTweetUser(final ResultSet resultSet) throws SQLException {
        TweetUser tweetUser = new TweetUser();
        tweetUser.setName(resultSet.getString("name"));
        tweetUser.setTweet(resultSet.getBoolean("is_tweet"));
        tweetUser.setCurTweets(resultSet.getInt("cur_tweet"));
        tweetUser.setMaxTweets(resultSet.getInt("max_tweet"));
        tweetUser.setNextTweet(resultSet.getTimestamp("next_tweet").getTime());
        tweetUser.setLastUpdateDay(resultSet.getInt("last_upd"));
        return tweetUser;
    }
}
