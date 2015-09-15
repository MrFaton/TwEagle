package com.mr_faton.core.dao;

import com.mr_faton.core.pool.db_connection.DBConnectionPool;
import com.mr_faton.core.table.TwitterMessage;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class TwitterMessageDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.TwitterMessageDAO");
    private static final String TABLE = "tweagle.twitter_messages";
    private static final int DEEP_YEARS_SEARCH = 3;
    private static final int DEEP_DAYS_SEARCH = 3;
    private final DBConnectionPool connectionPool;

    public TwitterMessageDAO(DBConnectionPool connectionPool) {
        logger.debug("constructor");
        this.connectionPool = connectionPool;
    }

    public void saveMessageList(List<TwitterMessage> messageList) throws SQLException {
        logger.debug("save message list");
    }

    public void updatePosted(TwitterMessage message) throws SQLException {
        logger.debug("set posted to true for the TwitterMessage with id " + message.getId());
        final String SQL = "" +
                "UPDATE " + TABLE + " SET posted = ? WHERE id = ?;";

        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, true);
        preparedStatement.setInt(2, message.getId());

        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }

    public TwitterMessage getTweet(boolean male) throws SQLException {
        logger.debug("get TwitterMessage");
        Connection connection = connectionPool.getConnection();
        //******* FIRST TRY *******
        TwitterMessage twitterMessage = getTweetFirstTry(male, connection);
        if (twitterMessage != null) {
            return twitterMessage;
        }

        //******* SECOND TRY *******
        twitterMessage = getTweetSecondTry(male, connection);
        if (twitterMessage != null) {
            return twitterMessage;
        }

        //******* THIRD TRY *******
        twitterMessage = getTweetThirdTry(male, connection);
        if (twitterMessage != null) {
            return twitterMessage;
        }

        //******* GET ANY TwitterMessage *******
        twitterMessage = getAnyTweet(male, connection);

        connection.close();

        return twitterMessage;
    }

    private TwitterMessage getTwitterMessageByResultSet(final ResultSet resultSet) throws SQLException{
        TwitterMessage twitterMessage = new TwitterMessage();

        twitterMessage.setId(resultSet.getInt("id"));
        twitterMessage.setMessage(resultSet.getString("message"));
        twitterMessage.setTweet(resultSet.getBoolean("tweet"));

        twitterMessage.setOwner(resultSet.getString("owner"));
        twitterMessage.setOwnerMale(resultSet.getBoolean("owner_male"));

        twitterMessage.setRecipient(resultSet.getString("recipient"));
        twitterMessage.setRecipientMale(resultSet.getBoolean("recipient_male"));

        twitterMessage.setPostedDate(resultSet.getDate("posted_date"));

        twitterMessage.setSynonymised(resultSet.getBoolean("synonymised"));
        twitterMessage.setPosted(resultSet.getBoolean("posted"));

        return twitterMessage;
    }



    private TwitterMessage getTweetFirstTry(boolean male, Connection connection) throws SQLException{
        logger.debug("get TwitterMessage - First try");
        final String SQL = "" +
                "SELECT * FROM " + TABLE + " WHERE " +
                "tweet = ? " +//1
                "AND " +
                "owner_male = ? " +//2
                "AND " +
                "posted_date = ? " +//3
                "AND " +
                "synonymised = ? " +//4
                "AND " +
                "posted = ? " +//5
                "LIMIT 1;";

        Calendar calendar = Calendar.getInstance();
        TwitterMessage twitterMessage = null;


        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        ResultSet resultSet = null;

        preparedStatement.setBoolean(1, true);
        preparedStatement.setBoolean(2, male);
        /*parameter 3 (posted_date) set in for-cycle*/
        preparedStatement.setBoolean(4, true);
        preparedStatement.setBoolean(5, false);

        for (int counter = 0; counter < DEEP_YEARS_SEARCH; counter++) {
            calendar.add(Calendar.YEAR, -1);
            Date passedDate = new Date(calendar.getTimeInMillis());

            preparedStatement.setDate(3, passedDate);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                twitterMessage = getTwitterMessageByResultSet(resultSet);
                logger.debug("found tweet at the first try in cycle " + counter +
                        " with date " + String.format("%td-%<tm-%<tY", passedDate));
                break;
            }
        }

        resultSet.close();
        preparedStatement.close();

        return twitterMessage;
    }

    private TwitterMessage getTweetSecondTry(boolean male, Connection connection) throws SQLException {
        logger.debug("get TwitterMessage - Second Try");
        final String SQL = "" +
                "SELECT * FROM " + TABLE + " WHERE " +
                "tweet = ? " +//1
                "AND " +
                "owner_male = ? " +//2
                "AND " +
                "posted_date >= ? " +//3
                "AND " +
                "posted_date <= ? " +//4
                "AND " +
                "synonymised = ? " +//5
                "AND " +
                "posted = ? " +//6
                "LIMIT 1;";

        TwitterMessage twitterMessage = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = null;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, true);
        preparedStatement.setBoolean(2, male);
        /*3 and 4 parameters (posted_date) set in for-cycle*/
        preparedStatement.setBoolean(5, true);
        preparedStatement.setBoolean(6, false);

        Calendar calendar = Calendar.getInstance();

        for (int counter = 0; counter < DEEP_YEARS_SEARCH; counter++) {
            calendar.add(Calendar.YEAR, -1);

            Calendar minDay = Calendar.getInstance();
            Calendar maxDay = Calendar.getInstance();

            minDay.setTimeInMillis(calendar.getTimeInMillis());
            maxDay.setTimeInMillis(calendar.getTimeInMillis());

            for (int doubleCounter = 0; doubleCounter < DEEP_DAYS_SEARCH; doubleCounter++) {
                minDay.add(Calendar.DAY_OF_MONTH, -1);
                maxDay.add(Calendar.DAY_OF_MONTH, 1);

                Date passedMinDay = new Date(minDay.getTimeInMillis());
                Date passedMaxDay = new Date(maxDay.getTimeInMillis());

                preparedStatement.setDate(3, passedMinDay);
                preparedStatement.setDate(4, passedMaxDay);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    twitterMessage = getTwitterMessageByResultSet(resultSet);
                    logger.debug("found tweet at the second try in general cycle " + counter +
                    " and inner cycle " + doubleCounter + " between dates " +
                            String.format("%td-%<tm-%<tY", passedMinDay) + " " +
                            "and " +
                            String.format("%td-%<tm-%<tY", passedMaxDay));
                    break;
                }
            }
            if (twitterMessage != null) {
                break;
            }
        }
        resultSet.close();
        preparedStatement.close();
        return twitterMessage;
    }

    private TwitterMessage getTweetThirdTry(boolean male, Connection connection) throws SQLException {
        logger.debug("get TwitterMessage - Third try");
        final String SQL = "" +
                "SELECT * FROM " + TABLE + " WHERE " +
                "tweet = ? " +//1
                "AND " +
                "owner_male = ? " +//2
                "AND " +
                "synonymised = ? " +//3
                "AND " +
                "posted = ? " +//4
                "LIMIT 1;";

        TwitterMessage twitterMessage = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, true);
        preparedStatement.setBoolean(2, male);
        preparedStatement.setBoolean(3, true);
        preparedStatement.setBoolean(4, false);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            twitterMessage = getTwitterMessageByResultSet(resultSet);
            logger.debug("found tweet at the third try, date " +
                    String.format("%td-%<tm-%<tY", twitterMessage.getPostedDate()));
        }

        resultSet.close();
        preparedStatement.close();

        return twitterMessage;
    }

    private TwitterMessage getAnyTweet(boolean male, Connection connection) throws SQLException {
        logger.debug("get TwitterMessage - Any TwitterMessage");
        final String SQL = "" +
                "SELECT * FROM " + TABLE + " WHERE " +
                "tweet = ? " +
                "AND " +
                "owner_male = ? " +
                "LIMIT 1;";

        TwitterMessage twitterMessage = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = null;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, true);
        preparedStatement.setBoolean(2, male);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            twitterMessage = getTwitterMessageByResultSet(resultSet);
            logger.debug("found tweet like any twitter message, date " +
                    String.format("%td-%<tm-%<tY", twitterMessage.getPostedDate()));
        }

        resultSet.close();
        preparedStatement.close();

        return twitterMessage;
    }
}
