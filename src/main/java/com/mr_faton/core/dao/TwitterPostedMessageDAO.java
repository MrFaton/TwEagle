package com.mr_faton.core.dao;

import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.table.TwitterPostedMessage;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class TwitterPostedMessageDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.TwitterPostedMessageDAO");
    private static final String TABLE = "tweagle.twitter_posted_messages";
    private final DBConnectionPool connectionPool;

    public TwitterPostedMessageDAO(DBConnectionPool connectionPool) {
        logger.debug("constructor");
        this.connectionPool = connectionPool;
    }

    public void savePostedMessage(TwitterPostedMessage twitterPostedMessage) throws Exception {
        logger.debug("save posted message");
        final String SQL = "" +
                "INSERT INTO " + TABLE + " (message, message_id, tweet, owner, recipient, posted_date) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setString(1, twitterPostedMessage.getMessage());
        preparedStatement.setLong(2, twitterPostedMessage.getMessageId());
        preparedStatement.setBoolean(3, twitterPostedMessage.isTweet());
        preparedStatement.setString(4, twitterPostedMessage.getOwner());
        preparedStatement.setString(5, twitterPostedMessage.getRecipient());
        Date postedDate = new Date(twitterPostedMessage.getPostedDate().getTime());
        preparedStatement.setDate(6, postedDate);

        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }
}
