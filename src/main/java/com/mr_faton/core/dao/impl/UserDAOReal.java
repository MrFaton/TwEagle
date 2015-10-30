package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 28.09.2015
 */
public class UserDAOReal implements UserDAO, Serializable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.UserDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.users " +
            "(name, password, email, male, creation_date, messages, following, followers, " +
            "consumer_key, consumer_secret, access_token, access_token_secret) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.users SET password = ?, email = ?, male = ?, creation_date = ?, " +
            "messages = ?, following = ?, followers = ?, consumer_key = ?, consumer_secret = ?, access_token = ?, " +
            "access_token_secret = ? WHERE name = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User getUserByName(String name) throws SQLException, NoSuchEntityException {
        logger.debug("get user by name " + name);
        final String SQL = "" +
                "SELECT * FROM tweagle.users WHERE name = ?;";
        try {
            return jdbcTemplate.queryForObject(SQL, new Object[]{name}, new UserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("user with name '" + name + "' not found", emptyData);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get all users");
        final String SQL = "" +
                "SELECT * FROM tweagle.users;";
        try {
            return jdbcTemplate.query(SQL, new UserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("users not exists in DB", emptyData);
        }
    }


    // INSERTS - UPDATES
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(final User user) throws SQLException {
        logger.info("save user " + user);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pS) throws SQLException {
                pS.setString(1, user.getName());
                pS.setString(2, user.getPassword());
                pS.setString(3, user.getEmail());
                pS.setBoolean(4, user.isMale());
                pS.setDate(5, new java.sql.Date(user.getCreationDate().getTime()));
                pS.setInt(6, user.getMessages());
                pS.setInt(7, user.getFollowing());
                pS.setInt(8, user.getFollowers());
                pS.setString(9, user.getConsumerKey());
                pS.setString(10, user.getConsumerSecret());
                pS.setString(11, user.getAccessToken());
                pS.setString(12, user.getAccessTokenSecret());
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(final List<User> userList) throws SQLException {
        logger.info("save  " + userList.size() + " users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pS, int i) throws SQLException {
                User user = userList.get(i);

                pS.setString(1, user.getName());
                pS.setString(2, user.getPassword());
                pS.setString(3, user.getEmail());
                pS.setBoolean(4, user.isMale());
                pS.setDate(5, new java.sql.Date(user.getCreationDate().getTime()));
                pS.setInt(6, user.getMessages());
                pS.setInt(7, user.getFollowing());
                pS.setInt(8, user.getFollowers());
                pS.setString(9, user.getConsumerKey());
                pS.setString(10, user.getConsumerSecret());
                pS.setString(11, user.getAccessToken());
                pS.setString(12, user.getAccessTokenSecret());
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(final User user) throws SQLException {
        logger.info("update user " + user);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pS) throws SQLException {
                pS.setString(1, user.getPassword());
                pS.setString(2, user.getEmail());
                pS.setBoolean(3, user.isMale());
                pS.setDate(4, new Date(user.getCreationDate().getTime()));
                pS.setInt(5, user.getMessages());
                pS.setInt(6, user.getFollowing());
                pS.setInt(7, user.getFollowers());
                pS.setString(8, user.getConsumerKey());
                pS.setString(9, user.getConsumerSecret());
                pS.setString(10, user.getAccessToken());
                pS.setString(11, user.getAccessTokenSecret());
                pS.setString(12, user.getName());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(final List<User> userList) throws SQLException {
        logger.info("update " + userList.size() + " users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pS, int i) throws SQLException {
                User user = userList.get(i);

                pS.setString(1, user.getPassword());
                pS.setString(2, user.getEmail());
                pS.setBoolean(3, user.isMale());
                pS.setDate(4, new Date(user.getCreationDate().getTime()));
                pS.setInt(5, user.getMessages());
                pS.setInt(6, user.getFollowing());
                pS.setInt(7, user.getFollowers());
                pS.setString(8, user.getConsumerKey());
                pS.setString(9, user.getConsumerSecret());
                pS.setString(10, user.getAccessToken());
                pS.setString(11, user.getAccessTokenSecret());
                pS.setString(12, user.getName());
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNamber) throws SQLException {
            User user = new User();
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setMale(resultSet.getBoolean("male"));
            user.setCreationDate(resultSet.getDate("creation_date"));
            user.setMessages(resultSet.getInt("messages"));
            user.setFollowing(resultSet.getInt("following"));
            user.setFollowers(resultSet.getInt("followers"));
            user.setConsumerKey(resultSet.getString("consumer_key"));
            user.setConsumerSecret(resultSet.getString("consumer_secret"));
            user.setAccessToken(resultSet.getString("access_token"));
            user.setAccessTokenSecret(resultSet.getString("access_token_secret"));
            return user;
        }
    }
}
