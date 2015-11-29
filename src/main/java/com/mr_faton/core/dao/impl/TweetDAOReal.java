package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Tweet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
public class TweetDAOReal implements TweetDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.TweetDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.tweets (owner_id, message, posted_date, synonymized, reposted) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.tweets SET message = ?, synonymized = ?, reposted = ? WHERE id = ?;";
    private static final String SQL_SELECT = "" +
            "SELECT * FROM tweagle.tweets " +
            "LEFT OUTER JOIN tweagle.posted_messages ON tweets.id = ";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Tweet getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        return null;
    }

    @Override
    public Tweet getTweet(boolean ownerMale, Date minDate, Date maxDate) throws SQLException, NoSuchEntityException {
        return null;
    }

    @Override
    public void save(final Tweet tweet) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tweet.getOwner());
                ps.setString(2, tweet.getMessage());
                ps.setTimestamp(3, new Timestamp(tweet.getPostedDate().getTime()));
                ps.setBoolean(4, tweet.isSynonymized());
                ps.setBoolean(5, tweet.isReposted());
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<Tweet> tweetList) throws SQLException {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Tweet tweet = tweetList.get(i);

                ps.setString(1, tweet.getOwner());
                ps.setString(2, tweet.getMessage());
                ps.setTimestamp(3, new Timestamp(tweet.getPostedDate().getTime()));
                ps.setBoolean(4, tweet.isSynonymized());
                ps.setBoolean(5, tweet.isReposted());
            }

            @Override
            public int getBatchSize() {
                return tweetList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }

    @Override
    public void update(final Tweet tweet) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tweet.getMessage());
                ps.setBoolean(2, tweet.isSynonymized());
                ps.setBoolean(3, tweet.isReposted());
                ps.setInt(4, tweet.getId());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Override
    public void update(final List<Tweet> tweetList) throws SQLException {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Tweet tweet = tweetList.get(i);

                ps.setString(1, tweet.getMessage());
                ps.setBoolean(2, tweet.isSynonymized());
                ps.setBoolean(3, tweet.isReposted());
                ps.setInt(4, tweet.getId());
            }

            @Override
            public int getBatchSize() {
                return tweetList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }
}
