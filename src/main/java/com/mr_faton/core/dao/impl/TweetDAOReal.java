package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.TweetDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Tweet;
import com.mr_faton.core.util.TimeWizard;
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
            "SELECT id, owner_id, male, message, posted_date, synonymized, reposted FROM tweagle.tweets " +
            "INNER JOIN tweagle.donor_users ON tweets.owner_id = donor_users.du_name ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Tweet getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        final String PREDICATE = "WHERE synonymized = 1 AND reposted = 0 AND male = " + (ownerMale ? 1 : 0) + " LIMIT 1;";
        final String SQL = SQL_SELECT + PREDICATE;
        try {
            return jdbcTemplate.queryForObject(SQL, new TweetRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("no tweet found with parameter: male =  '" + ownerMale + "'");
        }
    }

    @Override
    public Tweet getTweet(boolean ownerMale, final Date minDate, final Date maxDate) throws SQLException, NoSuchEntityException {
        final String PREDICATE = "WHERE synonymized = 1 AND reposted = 0 AND posted_date BETWEEN ? AND ? LIMIT 1;";
        final String SQL = SQL_SELECT + PREDICATE;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setTimestamp(1, new Timestamp(minDate.getTime()));
                ps.setTimestamp(2, new Timestamp(maxDate.getTime()));
            }
        };
        List<Tweet> query = jdbcTemplate.query(SQL, pss, new TweetRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("no tweet found with parameters:" +
                    "male = '" + ownerMale + "', " +
                    "between '" + TimeWizard.formatDateWithTime(minDate.getTime()) + "' " +
                    "and '" + TimeWizard.formatDateWithTime(maxDate.getTime()) + "'");
        }
        return query.get(0);
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

    private class TweetRowMapper implements RowMapper<Tweet> {
        @Override
        public Tweet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Tweet tweet = new Tweet();

            tweet.setId(resultSet.getInt("id"));
            tweet.setOwner(resultSet.getString("owner_id"));
            tweet.setOwnerMale(resultSet.getBoolean("male"));
            tweet.setMessage(resultSet.getString("message"));
            tweet.setPostedDate(resultSet.getTimestamp("posted_date"));
            tweet.setSynonymized(resultSet.getBoolean("synonymized"));
            tweet.setReposted(resultSet.getBoolean("reposted"));

            return tweet;
        }
    }
}
