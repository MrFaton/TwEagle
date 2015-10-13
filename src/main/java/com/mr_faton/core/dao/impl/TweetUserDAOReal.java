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
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.tweet_users (name, is_tweet, cur_tweet, max_tweet, next_tweet, last_upd) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.tweet_users SET " +
            "is_tweet = ?, cur_tweet = ?, max_tweet = ?, next_tweet = ?, last_upd = ? WHERE name = ?;";

    private final DataSource dataSource;

    public TweetUserDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public TweetUser getUserForTweet() throws SQLException, NoSuchEntityException {
        logger.debug("get tweetUser for posting tweets");
        final String SQL = "" +
                "SELECT * FROM tweagle.tweet_users WHERE " +
                "is_tweet = 1 AND cur_tweet < max_tweet AND " +
                "next_tweet = (SELECT MIN(next_tweet) FROM tweagle.tweet_users);";
        Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) return getTweetUser(resultSet);
            throw new NoSuchEntityException();
        }
    }

    @Override
    public List<TweetUser> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get tweet users");
        final String SQL = "SELECT * FROM tweagle.tweet_users;";
        List<TweetUser> tweetUserList = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {

            while (resultSet.next()) {
                tweetUserList.add(getTweetUser(resultSet));
            }
        }
        if (tweetUserList.isEmpty()) throw new NoSuchEntityException();
        return tweetUserList;
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


    // INSERTS - UPDATES
    @Override
    public void save(TweetUser tweetUser) throws SQLException {
        logger.debug("save tweet tweetUser " + tweetUser);
        Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, tweetUser.getName());
            preparedStatement.setBoolean(2, tweetUser.isTweet());
            preparedStatement.setInt(3, tweetUser.getCurTweets());
            preparedStatement.setInt(4, tweetUser.getMaxTweets());
            preparedStatement.setTimestamp(5, new Timestamp(tweetUser.getNextTweet()));
            preparedStatement.setInt(6, tweetUser.getLastUpdateDay());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<TweetUser> tweetUserList) throws SQLException {
        logger.debug("save " + tweetUserList.size() + " tweet users");
        Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (TweetUser tweetUser : tweetUserList) {
                preparedStatement.setString(1, tweetUser.getName());
                preparedStatement.setBoolean(2, tweetUser.isTweet());
                preparedStatement.setInt(3, tweetUser.getCurTweets());
                preparedStatement.setInt(4, tweetUser.getMaxTweets());
                preparedStatement.setTimestamp(5, new Timestamp(tweetUser.getNextTweet()));
                preparedStatement.setInt(6, tweetUser.getLastUpdateDay());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }


    @Override
    public void update(TweetUser tweetUser) throws SQLException {
        logger.debug("update tweet tweetUser " + tweetUser);
        Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setBoolean(1, tweetUser.isTweet());
            preparedStatement.setInt(2, tweetUser.getCurTweets());
            preparedStatement.setInt(3, tweetUser.getMaxTweets());
            preparedStatement.setTimestamp(4, new Timestamp(tweetUser.getNextTweet()));
            preparedStatement.setInt(5, tweetUser.getLastUpdateDay());
            preparedStatement.setString(6, tweetUser.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<TweetUser> userList) throws SQLException {
        logger.debug("update " + userList.size() + " tweet users");
        Connection connection = dataSource.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
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
}
