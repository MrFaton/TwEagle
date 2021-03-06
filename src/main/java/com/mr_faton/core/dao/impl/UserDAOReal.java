package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.User;
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
public class UserDAOReal implements UserDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.UserDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.users (name, password, email, male, creation_date, messages, following, followers, " +
            "consumer_key, consumer_secret, access_token, access_token_secret) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.users SET password = ?, email = ?, male = ?, creation_date = ?, " +
            "messages = ?, following = ?, followers = ?, consumer_key = ?, consumer_secret = ?, access_token = ?, " +
            "access_token_secret = ? WHERE name = ?;";

    private final DataSource dataSource;

    public UserDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public User getUserByName(String name) throws SQLException, NoSuchEntityException {
        logger.debug("get user by name " + name);
        final String SQL = "" +
                "SELECT * FROM tweagle.users WHERE name = ?;";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = getUser(resultSet);
                resultSet.close();
                return user;
            } else {
                resultSet.close();
                throw new NoSuchEntityException();
            }
        }

    }

    @Override
    public List<User> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get all users");
        final String SQL = "" +
                "SELECT * FROM tweagle.users;";
        List<User> userList = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {

            while (resultSet.next()) {
                userList.add(getUser(resultSet));
            }
        }
        if (userList.isEmpty()) throw new NoSuchEntityException();
        return userList;
    }




    private User getUser(ResultSet resultSet) throws SQLException {
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

    // INSERTS - UPDATES
    @Override
    public void save(User user) throws SQLException {
        logger.debug("save user " + user);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setBoolean(4, user.isMale());
            preparedStatement.setDate(5, new java.sql.Date(user.getCreationDate().getTime()));
            preparedStatement.setInt(6, user.getMessages());
            preparedStatement.setInt(7, user.getFollowing());
            preparedStatement.setInt(8, user.getFollowers());
            preparedStatement.setString(9, user.getConsumerKey());
            preparedStatement.setString(10, user.getConsumerSecret());
            preparedStatement.setString(11, user.getAccessToken());
            preparedStatement.setString(12, user.getAccessTokenSecret());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<User> userList) throws SQLException {
        logger.debug("save " + userList.size() + " users");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (User user : userList) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setBoolean(4, user.isMale());
                preparedStatement.setDate(5, new java.sql.Date(user.getCreationDate().getTime()));
                preparedStatement.setInt(6, user.getMessages());
                preparedStatement.setInt(7, user.getFollowing());
                preparedStatement.setInt(8, user.getFollowers());
                preparedStatement.setString(9, user.getConsumerKey());
                preparedStatement.setString(10, user.getConsumerSecret());
                preparedStatement.setString(11, user.getAccessToken());
                preparedStatement.setString(12, user.getAccessTokenSecret());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }



    @Override
    public void update(User user) throws SQLException {
        logger.debug("update user " + user);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setBoolean(3, user.isMale());
            preparedStatement.setDate(4, new java.sql.Date(user.getCreationDate().getTime()));
            preparedStatement.setInt(5, user.getMessages());
            preparedStatement.setInt(6, user.getFollowing());
            preparedStatement.setInt(7, user.getFollowers());
            preparedStatement.setString(8, user.getConsumerKey());
            preparedStatement.setString(9, user.getConsumerSecret());
            preparedStatement.setString(10, user.getAccessToken());
            preparedStatement.setString(11, user.getAccessTokenSecret());
            preparedStatement.setString(12, user.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<User> userList) throws SQLException {
        logger.debug("update " + userList.size() + " users");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            for (User user : userList) {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setBoolean(3, user.isMale());
                preparedStatement.setDate(4, new java.sql.Date(user.getCreationDate().getTime()));
                preparedStatement.setInt(5, user.getMessages());
                preparedStatement.setInt(6, user.getFollowing());
                preparedStatement.setInt(7, user.getFollowers());
                preparedStatement.setString(8, user.getConsumerKey());
                preparedStatement.setString(9, user.getConsumerSecret());
                preparedStatement.setString(10, user.getAccessToken());
                preparedStatement.setString(11, user.getAccessTokenSecret());
                preparedStatement.setString(12, user.getName());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
