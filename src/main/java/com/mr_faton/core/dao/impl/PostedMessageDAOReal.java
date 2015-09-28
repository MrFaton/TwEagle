package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.table.PostedMessage;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
    private final DataSource dataSource;

    public PostedMessageDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public void savePostedMessage(PostedMessage postedMessage) throws SQLException {
        logger.debug("save posted message");
        final String SQL = "" +
                "INSERT INTO tweagle.posted_messages (message, message_id, tweet, owner, recipient, posted_date) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, postedMessage.getMessage());
        preparedStatement.setLong(2, postedMessage.getMessageId());
        preparedStatement.setBoolean(3, postedMessage.isTweet());
        preparedStatement.setString(4, postedMessage.getOwner());
        preparedStatement.setString(5, postedMessage.getRecipient());
        preparedStatement.setTimestamp(6, new Timestamp(postedMessage.getPostedDate().getTime()));

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }
}
