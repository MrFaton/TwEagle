package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
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
            "UPDATE tweagle.messages SET message = ?, synonymized = ?, posted = ? WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Get Tweet
    @Override
    public Message getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "du_name AS owner, male AS owner_male, NULL AS recipient, NULL AS recipient_male " +
                "FROM tweagle.messages " +
                "INNER JOIN tweagle.donor_users ON messages.owner_id = donor_users.du_name " +
                "WHERE recipient_id IS NULL AND male = " + (ownerMale ? 1 : 0) + " LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(SQL, new MessageRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems no tweet found with parameters: " +
                    "ownerMale = '" + ownerMale + "'", emptyData);
        }
    }

    @Override
    public Message getTweet(final boolean ownerMale, final Calendar minCalendar, final Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "du_name AS owner, male AS owner_male, NULL AS recipient, NULL AS recipient_male " +
                "FROM tweagle.messages INNER JOIN tweagle.donor_users ON messages.owner_id = donor_users.du_name " +
                "WHERE recipient_id IS NULL AND male = ? AND posted_date BETWEEN ? AND ? LIMIT 1;";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, ownerMale);
                ps.setTimestamp(2, new Timestamp(minCalendar.getTimeInMillis()));
                ps.setTimestamp(3, new Timestamp(maxCalendar.getTimeInMillis()));
            }
        };
        List<Message> query = jdbcTemplate.query(SQL, pss, new MessageRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("it's seems no tweet found with parameters: " +
                    "ownerMale = '" + ownerMale + "', " +
                    "between '" + TimeWizard.formatDateWithTime(minCalendar.getTimeInMillis()) + "' and " +
                    "'" + TimeWizard.formatDateWithTime(maxCalendar.getTimeInMillis()) + "'");
        }
        return query.get(0);
    }

    @Override
    public Message getTweet(final String ownerName) throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "du_name AS 'owner', male AS 'owner_male', NULL AS 'recipient', NULL AS 'recipient_male'" +
                "FROM tweagle.messages INNER JOIN donor_users ON messages.owner_id = donor_users.du_name " +
                "WHERE recipient_id IS NULL AND du_name = '" + ownerName + "' LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(SQL, new MessageRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems no tweet found with parameters: " +
                    "ownerName = '" + ownerName + "'", emptyData);
        }
    }

    @Override
    public Message getTweet(final String ownerName, final Calendar minCalendar, final Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "du_name AS 'owner', male AS 'owner_male', NULL AS 'recipient', NULL AS 'recipient_male' " +
                "FROM tweagle.messages INNER JOIN donor_users ON messages.owner_id = donor_users.du_name " +
                "WHERE recipient_id IS NULL AND du_name = ? AND posted_date BETWEEN ? AND ? LIMIT 1;";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, ownerName);
                ps.setTimestamp(2, new Timestamp(minCalendar.getTimeInMillis()));
                ps.setTimestamp(3, new Timestamp(maxCalendar.getTimeInMillis()));
            }
        };
        List<Message> query = jdbcTemplate.query(SQL, pss, new MessageRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("it's seems no tweet found with parameters: " +
                    "ownerName = '" + ownerName + "', " +
                    "between '" + TimeWizard.formatDateWithTime(minCalendar.getTimeInMillis()) + "' and " +
                    "'" + TimeWizard.formatDateWithTime(maxCalendar.getTimeInMillis()) + "'");
        }
        return query.get(0);
    }



    //Get Mention
    @Override
    public Message getMention(
            final boolean ownerMale, final boolean recipientMale, final Calendar minCalendar, final Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "owner.du_name AS 'owner', owner.male AS 'owner_male', " +
                "recipient.du_name AS 'recipient', recipient.male AS 'recipient_male' " +
                "FROM tweagle.messages " +
                "INNER JOIN tweagle.donor_users owner ON messages.owner_id = owner.du_name " +
                "INNER JOIN tweagle.donor_users recipient ON messages.recipient_id = recipient.du_name " +
                "WHERE owner.male = ? AND recipient.male = ? AND  posted_date BETWEEN ? AND ? LIMIT 1;";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, ownerMale);
                ps.setBoolean(2, recipientMale);
                ps.setTimestamp(3, new Timestamp(minCalendar.getTimeInMillis()));
                ps.setTimestamp(4, new Timestamp(maxCalendar.getTimeInMillis()));
            }
        };

        List<Message> query = jdbcTemplate.query(SQL, pss, new MessageRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("it's seems no mention found with parameters: " +
                    "ownerMale = '" + ownerMale + "', " +
                    "recipientMale = '" + recipientMale + "', " +
                    "between '" + TimeWizard.formatDateWithTime(minCalendar.getTimeInMillis()) + "' and " +
                    "'" + TimeWizard.formatDateWithTime(maxCalendar.getTimeInMillis()) + "'");
        }
        return query.get(0);
    }


    @Override
    public List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException {
        logger.debug("get " + limit + " unsynonymized messages");
        final String SQL = "" +
                "SELECT messages.id, messages.message, messages.posted_date, messages.synonymized, messages.posted, " +
                "owner.du_name AS 'owner', owner.male AS 'owner_male', " +
                "recipient.du_name AS 'recipient', recipient.male AS 'recipient_male' " +
                "FROM tweagle.messages " +
                "INNER JOIN tweagle.donor_users owner ON messages.owner_id = owner.du_name " +
                "INNER JOIN tweagle.donor_users recipient ON messages.recipient_id = recipient.du_name " +
                "WHERE messages.synonymized = 0 LIMIT " + limit + ";";
        List<Message> query = jdbcTemplate.query(SQL, new MessageRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("it's seems no unsynonymized messages found");
        }
        return query;
    }


    // INSERTS - UPDATES
    @Override
    public void save(final Message message) throws SQLException {
        logger.info("save message " + message);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, message.getMessage());
                ps.setString(2, message.getOwner());
                if (message.getRecipient() == null) {
                    ps.setNull(3, Types.VARCHAR);
                } else {
                    ps.setString(3, message.getRecipient());
                }
                ps.setTimestamp(4, new Timestamp(message.getPostedDate().getTime()));
                ps.setBoolean(5, message.isSynonymized());
                ps.setBoolean(6, message.isPosted());
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<Message> messageList) throws SQLException {
        logger.info("save " + messageList.size() + " messages");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Message message = messageList.get(i);

                ps.setString(1, message.getMessage());
                ps.setString(2, message.getOwner());
                if (message.getRecipient() == null) {
                    ps.setNull(3, Types.VARCHAR);
                } else {
                    ps.setString(3, message.getRecipient());
                }
                ps.setTimestamp(4, new Timestamp(message.getPostedDate().getTime()));
                ps.setBoolean(5, message.isSynonymized());
                ps.setBoolean(6, message.isPosted());
            }

            @Override
            public int getBatchSize() {
                return messageList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }


    @Override
    public void update(final Message message) throws SQLException {
        logger.info("update message " + message);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, message.getMessage());
                ps.setBoolean(2, message.isSynonymized());
                ps.setBoolean(3, message.isPosted());
                ps.setInt(4, message.getId());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Override
    public void update(final List<Message> messageList) throws SQLException {
        logger.info("update " + messageList.size() + " messages");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Message message = messageList.get(i);

                ps.setString(1, message.getMessage());
                ps.setBoolean(2, message.isSynonymized());
                ps.setBoolean(3, message.isPosted());
                ps.setInt(4, message.getId());
            }

            @Override
            public int getBatchSize() {
                return messageList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }

    class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Message message = new Message();

            message.setId(resultSet.getInt("id"));
            message.setMessage(resultSet.getString("message"));

            message.setOwner(resultSet.getString("owner"));
            message.setOwnerMale(resultSet.getBoolean("owner_male"));

            message.setRecipient(resultSet.getString("recipient"));
            if (resultSet.getString("recipient") != null) {
                message.setRecipientMale(resultSet.getBoolean("recipient_male"));
            }

            message.setPostedDate(resultSet.getTimestamp("posted_date"));

            message.setSynonymized(resultSet.getBoolean("synonymized"));
            message.setPosted(resultSet.getBoolean("posted"));

            return message;
        }
    }
}