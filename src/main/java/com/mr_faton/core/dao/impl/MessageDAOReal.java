package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.messages " +
            "(message, tweet, owner, owner_male, recipient, recipient_male, posted_date, synonymized, posted) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.messages SET message = ?, tweet = ?, owner = ?, owner_male = ?, recipient = ?, " +
            "recipient_male = ?, posted_date = ?, synonymized = ?, posted = ? WHERE id = ?;";

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
                "tweet = 1 AND owner_male = ? AND posted_date = ? AND synonymized = 1 AND posted = 0 LIMIT 1;";

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
                            " and inner cycle №" + doubleCounter + " between dates " +
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
    public List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException {
        logger.debug("get UnSynonymized Messages");
        final String SQL = "" +
                "SELECT * FROM tweagle.messages WHERE synonymized = 0 LIMIT " + limit + ";";
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        List<Message> unsynonymizedMessages = new ArrayList<>();
        while (resultSet.next()) {
            unsynonymizedMessages.add(getMessageByResultSet(resultSet));
        }

        resultSet.close();
        statement.close();

        if (unsynonymizedMessages.isEmpty()) throw new NoSuchEntityException("no unsynonymized messages");
        return unsynonymizedMessages;
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


    // INSERTS - UPDATES
    @Override
    public void save(Message message) throws SQLException {
        logger.debug("save message " + message);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setBoolean(2, message.isTweet());
            preparedStatement.setString(3, message.getOwner());
            preparedStatement.setBoolean(4, message.isOwnerMale());
            if (message.getRecipient() != null) {
                preparedStatement.setString(5, message.getRecipient());
                preparedStatement.setBoolean(6, message.isRecipientMale());
            } else {
                preparedStatement.setNull(5, Types.VARCHAR);
                preparedStatement.setNull(6, Types.BOOLEAN);
            }
            preparedStatement.setDate(7, new java.sql.Date(message.getPostedDate().getTime()));
            preparedStatement.setBoolean(8, message.isSynonymized());
            preparedStatement.setBoolean(9, message.isPosted());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<Message> messageList) throws SQLException {
        logger.debug("save " + messageList.size() + " messages");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (Message message : messageList) {
                preparedStatement.setString(1, message.getMessage());
                preparedStatement.setBoolean(2, message.isTweet());
                preparedStatement.setString(3, message.getOwner());
                preparedStatement.setBoolean(4, message.isOwnerMale());
                if (message.getRecipient() != null) {
                    preparedStatement.setString(5, message.getRecipient());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                }
                if (message.isRecipientMale() != null) {
                    preparedStatement.setBoolean(6, message.isRecipientMale());
                } else {
                    preparedStatement.setNull(6, Types.BOOLEAN);
                }
                preparedStatement.setDate(7, new java.sql.Date(message.getPostedDate().getTime()));
                preparedStatement.setBoolean(8, message.isSynonymized());
                preparedStatement.setBoolean(9, message.isPosted());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }


    @Override
    public void update(Message message) throws SQLException {
        logger.debug("update message " + message);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setBoolean(2, message.isTweet());
            preparedStatement.setString(3, message.getOwner());
            preparedStatement.setBoolean(4, message.isOwnerMale());
            if (message.getRecipient() != null) {
                preparedStatement.setString(5, message.getRecipient());
                preparedStatement.setBoolean(6, message.isRecipientMale());
            } else {
                preparedStatement.setNull(5, Types.VARCHAR);
                preparedStatement.setNull(6, Types.BOOLEAN);
            }
            preparedStatement.setDate(7, new java.sql.Date(message.getPostedDate().getTime()));
            preparedStatement.setBoolean(8, message.isSynonymized());
            preparedStatement.setBoolean(9, message.isPosted());
            preparedStatement.setInt(10, message.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<Message> messageList) throws SQLException {
        logger.debug("update " + messageList.size() + " messages");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            for (Message message : messageList) {
                preparedStatement.setString(1, message.getMessage());
                preparedStatement.setBoolean(2, message.isTweet());
                preparedStatement.setString(3, message.getOwner());
                preparedStatement.setBoolean(4, message.isOwnerMale());
                if (message.getRecipient() != null) {
                    preparedStatement.setString(5, message.getRecipient());
                    preparedStatement.setBoolean(6, message.isRecipientMale());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                    preparedStatement.setNull(6, Types.BOOLEAN);
                }
                preparedStatement.setDate(7, new java.sql.Date(message.getPostedDate().getTime()));
                preparedStatement.setBoolean(8, message.isSynonymized());
                preparedStatement.setBoolean(9, message.isPosted());
                preparedStatement.setInt(10, message.getId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
