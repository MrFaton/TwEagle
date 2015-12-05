package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.TweetUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
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
            "INSERT INTO tweagle.tweet_users (tu_name, is_tweet, cur_tweet, max_tweet, next_tweet, last_upd) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.tweet_users SET " +
            "is_tweet = ?, cur_tweet = ?, max_tweet = ?, next_tweet = ?, last_upd = ? WHERE tu_name = ?;";
    private static final String SQL_SELECT = "" +
            "SELECT * FROM tweagle.tweet_users ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TweetUser getUserForTweet() throws SQLException, NoSuchEntityException {
        logger.debug("get tweetUser for posting tweets");
        final String PREDICATE = "" +
                "WHERE next_tweet = " +
                "(SELECT MIN(next_tweet) FROM tweagle.tweet_users WHERE is_tweet = 1 AND cur_tweet < max_tweet);";
        final String SQL = SQL_SELECT + PREDICATE;
        try {
            return jdbcTemplate.queryForObject(SQL, new TwitterUserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("no twitter user for posting tweet found", emptyData);
        }
    }

    @Override
    public List<TweetUser> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get tweet users");
        List<TweetUser> tweetUserList = jdbcTemplate.query(SQL_SELECT, new TwitterUserRowMapper());
        if (tweetUserList.isEmpty()) throw new NoSuchEntityException("no tweet users found");
        return tweetUserList;
    }



    // INSERTS - UPDATES
    @Override
    public void save(final TweetUser tweetUser) throws SQLException {
        logger.debug("save tweet tweetUser " + tweetUser);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tweetUser.getName());
                ps.setBoolean(2, tweetUser.isTweet());
                ps.setInt(3, tweetUser.getCurTweets());
                ps.setInt(4, tweetUser.getMaxTweets());
                ps.setTimestamp(5, new Timestamp(tweetUser.getNextTweet().getTime()));
                ps.setInt(6, tweetUser.getLastUpdateDay());
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<TweetUser> tweetUserList) throws SQLException {
        logger.debug("save " + tweetUserList.size() + " tweet users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TweetUser tweetUser = tweetUserList.get(i);

                ps.setString(1, tweetUser.getName());
                ps.setBoolean(2, tweetUser.isTweet());
                ps.setInt(3, tweetUser.getCurTweets());
                ps.setInt(4, tweetUser.getMaxTweets());
                ps.setTimestamp(5, new Timestamp(tweetUser.getNextTweet().getTime()));
                ps.setInt(6, tweetUser.getLastUpdateDay());
            }

            @Override
            public int getBatchSize() {
                return tweetUserList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }


    @Override
    public void update(final TweetUser tweetUser) throws SQLException {
        logger.debug("update tweet tweetUser " + tweetUser);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, tweetUser.isTweet());
                ps.setInt(2, tweetUser.getCurTweets());
                ps.setInt(3, tweetUser.getMaxTweets());
                ps.setTimestamp(4, new Timestamp(tweetUser.getNextTweet().getTime()));
                ps.setInt(5, tweetUser.getLastUpdateDay());
                ps.setString(6, tweetUser.getName());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Override
    public void update(final List<TweetUser> userList) throws SQLException {
        logger.debug("update " + userList.size() + " tweet users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TweetUser tweetUser = userList.get(i);

                ps.setBoolean(1, tweetUser.isTweet());
                ps.setInt(2, tweetUser.getCurTweets());
                ps.setInt(3, tweetUser.getMaxTweets());
                ps.setTimestamp(4, new Timestamp(tweetUser.getNextTweet().getTime()));
                ps.setInt(5, tweetUser.getLastUpdateDay());
                ps.setString(6, tweetUser.getName());
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }

    class TwitterUserRowMapper implements RowMapper<TweetUser> {
        @Override
        public TweetUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            TweetUser tweetUser = new TweetUser();
            tweetUser.setId(resultSet.getInt("id"));
            tweetUser.setName(resultSet.getString("tu_name"));
            tweetUser.setTweet(resultSet.getBoolean("is_tweet"));
            tweetUser.setCurTweets(resultSet.getInt("cur_tweet"));
            tweetUser.setMaxTweets(resultSet.getInt("max_tweet"));

            Timestamp timestamp = resultSet.getTimestamp("next_tweet");
            if (timestamp != null) {
                tweetUser.setNextTweet(new Date(timestamp.getTime()));
            }else {
                tweetUser.setNextTweet(null);
            }

            tweetUser.setLastUpdateDay(resultSet.getInt("last_upd"));
            return tweetUser;
        }
    }
}