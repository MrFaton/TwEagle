package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.table.PostedMessage;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 28.09.2015
 */
public class PostedMessageDAOReal implements PostedMessageDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.PostedMessageDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.posted_messages (message, message_id, tweet, owner, recipient, posted_date) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.posted_messages SET message = ?, message_id = ?, tweet = ?, owner = ?, recipient = ?, " +
            "posted_date = ? WHERE id = ?;";

    private final DataSource dataSource;

    public PostedMessageDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }


    // INSERTS - UPDATES
    @Override
    public void save(PostedMessage postedMessage) throws SQLException {
        logger.debug("save posted message " + postedMessage);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, postedMessage.getMessage());
            preparedStatement.setLong(2, postedMessage.getMessageId());
            preparedStatement.setBoolean(3, postedMessage.isTweet());
            preparedStatement.setString(4, postedMessage.getOwner());
            if (postedMessage.getRecipient() != null) {
                preparedStatement.setString(5, postedMessage.getRecipient());
            } else {
                preparedStatement.setNull(5, Types.BOOLEAN);
            }
            preparedStatement.setDate(6, new java.sql.Date(postedMessage.getPostedDate().getTime()));

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<PostedMessage> postedMessageList) throws SQLException {
        logger.debug("save " + postedMessageList.size() + " posted messages");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE);) {
            for (PostedMessage postedMessage : postedMessageList) {
                preparedStatement.setString(1, postedMessage.getMessage());
                preparedStatement.setLong(2, postedMessage.getMessageId());
                preparedStatement.setBoolean(3, postedMessage.isTweet());
                preparedStatement.setString(4, postedMessage.getOwner());
                if (postedMessage.getRecipient() != null) {
                    preparedStatement.setString(5, postedMessage.getRecipient());
                } else {
                    preparedStatement.setNull(5, Types.BOOLEAN);
                }
                preparedStatement.setDate(6, new java.sql.Date(postedMessage.getPostedDate().getTime()));

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }



    @Override
    public void update(PostedMessage postedMessage) throws SQLException {
        logger.debug("update posted message " + postedMessage);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);) {
            preparedStatement.setString(1, postedMessage.getMessage());
            preparedStatement.setLong(2, postedMessage.getMessageId());
            preparedStatement.setBoolean(3, postedMessage.isTweet());
            preparedStatement.setString(4, postedMessage.getOwner());
            if (postedMessage.getRecipient() != null) {
                preparedStatement.setString(5, postedMessage.getRecipient());
            } else {
                preparedStatement.setNull(5, Types.BOOLEAN);
            }
            preparedStatement.setDate(6, new java.sql.Date(postedMessage.getPostedDate().getTime()));
            preparedStatement.setInt(7, postedMessage.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<PostedMessage> postedMessageList) throws SQLException {
        logger.debug("update " + postedMessageList.size() + " posted messages");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE);) {
            for (PostedMessage postedMessage : postedMessageList) {
                preparedStatement.setString(1, postedMessage.getMessage());
                preparedStatement.setLong(2, postedMessage.getMessageId());
                preparedStatement.setBoolean(3, postedMessage.isTweet());
                preparedStatement.setString(4, postedMessage.getOwner());
                if (postedMessage.getRecipient() != null) {
                    preparedStatement.setString(5, postedMessage.getRecipient());
                } else {
                    preparedStatement.setNull(5, Types.BOOLEAN);
                }
                preparedStatement.setDate(6, new java.sql.Date(postedMessage.getPostedDate().getTime()));
                preparedStatement.setInt(7, postedMessage.getId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
