package com.mr_faton.core.dao;

import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.table.TwitterDonor;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TwitterDonorDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.TwitterDonorDAO");
    private static final String TABLE = "tweagle.twitter_donors";
    private final DBConnectionPool connectionPool;

    public TwitterDonorDAO(DBConnectionPool connectionPool) {
        logger.debug("constructor");
        this.connectionPool = connectionPool;
    }

    public TwitterDonor getMessageDonor(boolean male) throws SQLException {
        logger.debug("get donor for parsing message");
        final String SQL = "" +
                "SELECT * FROM " + TABLE + " WHERE take_messages = 0 LIMIT 1;";

        TwitterDonor twitterDonor = null;

        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        if (resultSet.next()) {
            twitterDonor = getTwitterDonor(resultSet);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return twitterDonor;
    }

    private TwitterDonor getTwitterDonor(final ResultSet resultSet) throws SQLException {
        TwitterDonor donor = new TwitterDonor();

        donor.setName(resultSet.getString("donor_name"));
        donor.setMale(resultSet.getBoolean("male"));

        donor.setTakeMessage(resultSet.getBoolean("take_messages"));
        donor.setTakeFollowing(resultSet.getBoolean("take_following"));
        donor.setTakeFollowers(resultSet.getBoolean("take_followers"));
        donor.setTakeStatus(resultSet.getBoolean("take_status"));

        donor.setTakeMessagesDate(resultSet.getDate("take_messages_date"));
        donor.setTakeFollowingDate(resultSet.getDate("take_following_date"));
        donor.setTakeFollowersDate(resultSet.getDate("take_followers_date"));
        donor.setTakeStatusDate(resultSet.getDate("take_status_date"));

        return donor;
    }
}
