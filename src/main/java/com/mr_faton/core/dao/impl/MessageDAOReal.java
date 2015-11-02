package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

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
            "INSERT INTO tweagle.messages (message, owner_id, recipient_id, posted_date, synonymized, posted) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.messages SET synonymized = ?, posted = ? WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Message getTweet(boolean male, String ownerName, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        StringBuilder builtSQL = new StringBuilder();
        builtSQL.append("SELECT ");
        builtSQL.append("messages.id, messages.message, messages.synonymized, messages.posted, ");
        builtSQL.append("du_name AS 'donor_name', du_male AS 'donor_male' ");
        if (ownerName == null) {
            builtSQL.append("FROM tweagle.messages, tweagle.donor_users donor ");
        } else {
            builtSQL.append("FROM tweagle.messages INNER JOIN donor_users donor ON messages.owner_id = donor.du_name ");
        }
        builtSQL.append("WHERE ");
        if (minCalendar != null && maxCalendar != null) {
            builtSQL.append("posted_date BETWEEN ");
            builtSQL.append("'").append(new java.sql.Timestamp(minCalendar.getTimeInMillis())).append("' AND ");
            builtSQL.append("'").append(new java.sql.Timestamp(maxCalendar.getTimeInMillis())).append("' AND ");
        }
        builtSQL.append("recipient_id IS NULL AND donor.du_male = ").append(male ? 1 : 0).append(" ");
        builtSQL.append("LIMIT 1;");

        System.out.println(builtSQL);
        return null;
    }

    @Override
    public Message getMention(boolean tweet, boolean male, String ownerName, String recipientName, Calendar minCalendar, Calendar maxCalendar) throws SQLException, NoSuchEntityException {
        StringBuilder builtSQL = new StringBuilder();
        builtSQL.append("SELECT ");
        builtSQL.append("messages.id, ");
        builtSQL.append("messages.message, ");
        builtSQL.append("donor.du_name AS 'donor_name', ");
        builtSQL.append("donor.du_male AS 'donor_male', ");
        builtSQL.append("donor.take_messages_date AS 'donor_take_messages_date', ");
        builtSQL.append("donor.take_following_date AS 'donor_take_following_date', ");
        builtSQL.append("donor.take_followers_date AS 'donor_take_followers_date' ");
        builtSQL.append("FROM tweagle.messages ");
        builtSQL.append("INNER JOIN donor_users donor ON messages.owner_id = donor.du_name");

        return null;
    }

//    @Override
//    public Message getTweetFirstTry(boolean male) throws SQLException, NoSuchEntityException{
//        logger.debug("get tweet - First try");
//        final String SQL = "" +
//                "SELECT * FROM tweagle.messages WHERE " +
//                "owner_male = ? AND recipient IS NULL AND posted_date = ? AND synonymized = 1 AND posted = 0 LIMIT 1;";
//
//        Calendar calendar = Calendar.getInstance();
//        Message message = null;
//
//
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setBoolean(1, male);
//        /*parameter 2 (posted_date) set in for-cycle*/
//
//            for (int counter = 0; counter < DEEP_YEARS_SEARCH; counter++) {
//                calendar.add(Calendar.YEAR, -1);
//                java.sql.Date passedDate = new Date(calendar.getTimeInMillis());
//
//                preparedStatement.setDate(2, passedDate);
//
//                try(ResultSet resultSet = preparedStatement.executeQuery();) {
//                    if (resultSet.next()) {
//                        message = getMessageByResultSet(resultSet);
//                        logger.info("found tweet at the first try in cycle №" + counter +
//                                " with date " + String.format("%td-%<tm-%<tY", passedDate));
//                        break;
//                    }
//                }
//            }
//        }
//        if (message == null) throw new NoSuchEntityException("tweet not found at the first try");
//        return message;
//    }
//    @Override
//    public Message getTweetSecondTry(boolean male) throws SQLException, NoSuchEntityException {
//        logger.debug("get tweet - Second Try");
//        final String SQL = "" +
//                "SELECT * FROM tweagle.messages WHERE " +
//                "owner_male = ? AND recipient IS NULL AND posted_date >= ? AND posted_date <= ? AND synonymized = 1 AND " +
//                "posted = 0 LIMIT 1;";
//
//        Message message = null;
//
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setBoolean(1, male);
//        /*3 and 4 parameters (posted_date) set in for-cycle*/
//
//            Calendar calendar = Calendar.getInstance();
//            for (int counter = 0; counter < DEEP_YEARS_SEARCH; counter++) {
//                calendar.add(Calendar.YEAR, -1);
//
//                Calendar minDay = Calendar.getInstance();
//                Calendar maxDay = Calendar.getInstance();
//
//                minDay.setTimeInMillis(calendar.getTimeInMillis());
//                maxDay.setTimeInMillis(calendar.getTimeInMillis());
//
//                for (int doubleCounter = 0; doubleCounter < DEEP_DAYS_SEARCH; doubleCounter++) {
//                    minDay.add(Calendar.DAY_OF_MONTH, -1);
//                    maxDay.add(Calendar.DAY_OF_MONTH, 1);
//
//                    java.sql.Date passedMinDay = new Date(minDay.getTimeInMillis());
//                    java.sql.Date passedMaxDay = new Date(maxDay.getTimeInMillis());
//
//                    preparedStatement.setDate(2, passedMinDay);
//                    preparedStatement.setDate(3, passedMaxDay);
//
//                    try(ResultSet resultSet = preparedStatement.executeQuery()) {
//                        if (resultSet.next()) {
//                            message = getMessageByResultSet(resultSet);
//                            logger.info("found tweet at the second try in general cycle №" + counter +
//                                    " and inner cycle №" + doubleCounter + " between dates " +
//                                    String.format("%td-%<tm-%<tY", passedMinDay) + " " +
//                                    "and " +
//                                    String.format("%td-%<tm-%<tY", passedMaxDay));
//                            break;
//                        }
//                    }
//                }
//                if (message != null) {
//                    break;
//                }
//            }
//        }
//        if (message == null) throw new NoSuchEntityException("tweet not found at the second try");
//        return message;
//    }
//    @Override
//    public Message getTweetThirdTry(boolean male) throws SQLException, NoSuchEntityException {
//        logger.debug("get tweet - Third try");
//        final String SQL = "" +
//                "SELECT * FROM tweagle.messages WHERE " +
//                "owner_male = ? AND recipient IS NULL AND synonymized = 1 AND posted = 0 LIMIT 1;";
//
//        Message message = null;
//
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setBoolean(1, male);
//            try(ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    message = getMessageByResultSet(resultSet);
//                    logger.info("found tweet at the third try, date " +
//                            String.format("%td-%<tm-%<tY", message.getPostedDate()));
//                }
//            }
//        }
//        if (message == null) throw new NoSuchEntityException("tweet not found at the third try");
//        return message;
//    }
//    @Override
//    public Message getAnyTweet(boolean male) throws SQLException {
//        logger.debug("get tweet - any");
//        final String SQL = "" +
//                "SELECT * FROM tweagle.messages WHERE owner_male = ? AND recipient IS NULL LIMIT 1;";
//
//        Message message = null;
//
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setBoolean(1, male);
//            try(ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    message = getMessageByResultSet(resultSet);
//                    logger.info("found tweet like any twitter message, date " +
//                            String.format("%td-%<tm-%<tY", message.getPostedDate()));
//                }
//            }
//
//        }
//        return message;
//    }
//
//
    @Override
    public List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException {
//        logger.debug("get UnSynonymized Messages");
//        final String SQL = "" +
//                "SELECT * FROM tweagle.messages WHERE synonymized = 0 LIMIT " + limit + ";";
//        Connection connection = dataSource.getConnection();
//        try(Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(SQL)) {
//            List<Message> unsynonymizedMessages = new ArrayList<>();
//            while (resultSet.next()) {
//                unsynonymizedMessages.add(getMessageByResultSet(resultSet));
//            }
//            if (unsynonymizedMessages.isEmpty()) throw new NoSuchEntityException("no unsynonymized messages");
//            return unsynonymizedMessages;
//        }
        return null;
    }
//
//    private Message getMessageByResultSet(final ResultSet resultSet) throws SQLException{
//        Message message = new Message();
//
//        message.setId(resultSet.getInt("id"));
//        message.setMessage(resultSet.getString("message"));
//
//        message.setOwner(resultSet.getString("owner"));
//        message.setOwnerMale(resultSet.getBoolean("owner_male"));
//
//        message.setRecipient(resultSet.getString("recipient"));
//        message.setRecipientMale(resultSet.getBoolean("recipient_male"));
//
//        message.setPostedDate(resultSet.getDate("posted_date"));
//
//        message.setSynonymized(resultSet.getBoolean("synonymized"));
//        message.setPosted(resultSet.getBoolean("posted"));
//
//        return message;
//    }


    // INSERTS - UPDATES
    @Override
    public void save(Message message) throws SQLException {
//        logger.info("save message " + message);
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
//            preparedStatement.setString(1, message.getMessage());
//            preparedStatement.setString(2, message.getOwner());
//            preparedStatement.setBoolean(3, message.isOwnerMale());
//            if (message.getRecipient() != null) {
//                preparedStatement.setString(4, message.getRecipient());
//                preparedStatement.setBoolean(5, message.isRecipientMale());
//            } else {
//                preparedStatement.setNull(4, Types.VARCHAR);
//                preparedStatement.setNull(5, Types.BOOLEAN);
//            }
//            preparedStatement.setDate(6, new java.sql.Date(message.getPostedDate().getTime()));
//            preparedStatement.setBoolean(7, message.isSynonymized());
//            preparedStatement.setBoolean(8, message.isPosted());
//
//            preparedStatement.executeUpdate();
//        }
    }

    @Override
    public void save(List<Message> messageList) throws SQLException {
//        logger.info("save " + messageList.size() + " messages");
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
//            for (Message message : messageList) {
//                preparedStatement.setString(1, message.getMessage());
//                preparedStatement.setString(2, message.getOwner());
//                preparedStatement.setBoolean(3, message.isOwnerMale());
//                if (message.getRecipient() != null) {
//                    preparedStatement.setString(4, message.getRecipient());
//                } else {
//                    preparedStatement.setNull(4, Types.VARCHAR);
//                }
//                preparedStatement.setNull(5, Types.BOOLEAN);
//                preparedStatement.setDate(6, new java.sql.Date(message.getPostedDate().getTime()));
//                preparedStatement.setBoolean(7, message.isSynonymized());
//                preparedStatement.setBoolean(8, message.isPosted());
//
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        }
    }


    @Override
    public void update(Message message) throws SQLException {
//        logger.info("update message " + message);
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
//            preparedStatement.setString(1, message.getMessage());
//            preparedStatement.setString(2, message.getOwner());
//            preparedStatement.setBoolean(3, message.isOwnerMale());
//            if (message.getRecipient() != null) {
//                preparedStatement.setString(4, message.getRecipient());
//                preparedStatement.setBoolean(5, message.isRecipientMale());
//            } else {
//                preparedStatement.setNull(4, Types.VARCHAR);
//                preparedStatement.setNull(5, Types.BOOLEAN);
//            }
//            preparedStatement.setDate(6, new java.sql.Date(message.getPostedDate().getTime()));
//            preparedStatement.setBoolean(7, message.isSynonymized());
//            preparedStatement.setBoolean(8, message.isPosted());
//            preparedStatement.setInt(9, message.getId());
//
//            preparedStatement.executeUpdate();
//        }
    }

    @Override
    public void update(List<Message> messageList) throws SQLException {
//        logger.info("update " + messageList.size() + " messages");
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
//            for (Message message : messageList) {
//                preparedStatement.setString(1, message.getMessage());
//                preparedStatement.setString(2, message.getOwner());
//                preparedStatement.setBoolean(3, message.isOwnerMale());
//                if (message.getRecipient() != null) {
//                    preparedStatement.setString(4, message.getRecipient());
//                    preparedStatement.setBoolean(5, message.isRecipientMale());
//                } else {
//                    preparedStatement.setNull(4, Types.VARCHAR);
//                    preparedStatement.setNull(5, Types.BOOLEAN);
//                }
//                preparedStatement.setDate(6, new java.sql.Date(message.getPostedDate().getTime()));
//                preparedStatement.setBoolean(7, message.isSynonymized());
//                preparedStatement.setBoolean(8, message.isPosted());
//                preparedStatement.setInt(9, message.getId());
//
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        }
    }

    public static void main(String[] args) throws SQLException, NoSuchEntityException {
        Calendar minCal = Calendar.getInstance();
        minCal.add(Calendar.DAY_OF_MONTH, -20);

        MessageDAOReal messageDAOReal = new MessageDAOReal();
        messageDAOReal.getTweet(true, null, null, null);
//        messageDAOReal.getTweet(true, "Tony", minCal, Calendar.getInstance());
    }
}
