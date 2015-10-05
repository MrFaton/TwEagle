package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 20.09.2015
 */
public class MessageDAOReal implements MessageDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.MessageDAOReal");

    private static final int DEEP_YEARS_SEARCH = 3;
    private static final int DEEP_DAYS_SEARCH = 3;

    private final DataSource dataSource;

    public MessageDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public Message getTweetFirstTry(boolean male) throws SQLException, NoSuchEntityException{
        logger.debug("get tweet - First try");
        final String SQL = "" +
                "SELECT * FROM tweagle.messages WHERE " +
                "tweet = 1 AND " +
                "owner_male = ? AND " +//1
                "posted_date = ? AND " +//2
                "synonymized = 1 AND " +
                "posted = 0 " +
                "LIMIT 1;";

        Calendar calendar = Calendar.getInstance();
        Message message = null;


        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = null;

        preparedStatement.setBoolean(1, male);
        /*parameter 2 (posted_date) set in for-cycle*/

        for (int counter = 0; counter < DEEP_YEARS_SEARCH; counter++) {
            calendar.add(Calendar.YEAR, -1);
            java.sql.Date passedDate = new Date(calendar.getTimeInMillis());

            preparedStatement.setDate(2, passedDate);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                message = getMessageByResultSet(resultSet);
                logger.info("found tweet at the first try in cycle №" + counter +
                        " with date " + String.format("%td-%<tm-%<tY", passedDate));
                break;
            }

        }

        resultSet.close();
        preparedStatement.close();

        if (message == null) throw new NoSuchEntityException("tweet not found at the first try");
        return message;
    }
    @Override
    public Message getTweetSecondTry(boolean male) throws SQLException, NoSuchEntityException {
        logger.debug("get tweet - Second Try");
        final String SQL = "" +
                "SELECT * FROM tweagle.messages WHERE " +
                "tweet = 1 AND " +
                "owner_male = ? AND " +//1
                "posted_date >= ? AND " +//2
                "posted_date <= ? AND " +//3
                "synonymized = 1 AND " +
                "posted = 0 " +
                "LIMIT 1;";

        Message message = null;

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet = null;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, male);
        /*3 and 4 parameters (posted_date) set in for-cycle*/

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

                java.sql.Date passedMinDay = new Date(minDay.getTimeInMillis());
                java.sql.Date passedMaxDay = new Date(maxDay.getTimeInMillis());

                preparedStatement.setDate(2, passedMinDay);
                preparedStatement.setDate(3, passedMaxDay);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    message = getMessageByResultSet(resultSet);
                    logger.info("found tweet at the second try in general cycle №" + counter +
                            " and inner cycle №" + doubleCounter + "between dates " +
                            String.format("%td-%<tm-%<tY", passedMinDay) + " " +
                            "and " +
                            String.format("%td-%<tm-%<tY", passedMaxDay));
                    break;
                }
            }
            if (message != null) {
                break;
            }
        }
        resultSet.close();
        preparedStatement.close();

        if (message == null) throw new NoSuchEntityException("tweet not found at the second try");
        return message;
    }
    @Override
    public Message getTweetThirdTry(boolean male) throws SQLException, NoSuchEntityException {
        logger.debug("get tweet - Third try");
        final String SQL = "" +
                "SELECT * FROM tweagle.messages WHERE " +
                "tweet = 1 AND " +
                "owner_male = ? AND " +//1
                "synonymized = 1 AND " +
                "posted = 0 " +
                "LIMIT 1;";

        Message message = null;

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, male);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            message = getMessageByResultSet(resultSet);
            logger.info("found tweet at the third try, date " +
                    String.format("%td-%<tm-%<tY", message.getPostedDate()));
        }

        resultSet.close();
        preparedStatement.close();

        if (message == null) throw new NoSuchEntityException("tweet not found at the third try");
        return message;
    }
    @Override
    public Message getAnyTweet(boolean male) throws SQLException {
        logger.debug("get tweet - any");
        final String SQL = "" +
                "SELECT * FROM tweagle.messages WHERE tweet = 1 AND owner_male = ? LIMIT 1;";

        Message message = null;

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        ResultSet resultSet;

        preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setBoolean(1, male);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            message = getMessageByResultSet(resultSet);
            logger.info("found tweet like any twitter message, date " +
                    String.format("%td-%<tm-%<tY", message.getPostedDate()));
        }

        resultSet.close();
        preparedStatement.close();

        return message;
    }


    @Override
    public void updatePostedMessage(Message message) throws SQLException {
        logger.debug("change posted status for message with id " + message.getId());
        final String SQL = "" +
                "UPDATE tweagle.messages SET posted = 1 WHERE id = ?;";

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, message.getId());

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }


    private Message getMessageByResultSet(final ResultSet resultSet) throws SQLException{
        Message message = new Message();

        message.setId(resultSet.getInt("id"));
        message.setMessage(resultSet.getString("message"));
        message.setTweet(resultSet.getBoolean("tweet"));

        message.setOwner(resultSet.getString("owner"));
        message.setOwnerMale(resultSet.getBoolean("owner_male"));

        message.setRecipient(resultSet.getString("recipient"));
        message.setRecipientMale(resultSet.getBoolean("recipient_male"));

        message.setPostedDate(resultSet.getDate("posted_date"));

        message.setSynonymized(resultSet.getBoolean("synonymized"));
        message.setPosted(resultSet.getBoolean("posted"));

        return message;
    }
}
