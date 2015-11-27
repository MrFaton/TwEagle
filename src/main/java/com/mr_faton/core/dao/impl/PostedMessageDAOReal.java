package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.PostedMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

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
            "INSERT INTO tweagle.posted_messages " +
            "(message_id, twitter_id, owner_id, recipient_id, retweeted, posted_date)  VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.posted_messages SET retweeted = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PostedMessage getUnRetweetedPostedMessage() throws SQLException, NoSuchEntityException {
        logger.debug("get unretweeted posted message");
        final String SQL = "" +
                "SELECT " +
                "posted_messages.id, " +

                "posted_messages.message_id, " +
                "messages.message, " +

                "posted_messages.twitter_id, " +

                "oldOwnerTable.du_name AS oldOwner, " +
                "oldOwnerTable.male AS oldOwnerMale, " +

                "oldRecipientTable.du_name AS oldRecipient, " +
                "oldRecipientTable.male AS oldRecipientMale, " +

                "posted_messages.owner_id, " +
                "ownerTable.male AS ownerMale, " +

                "posted_messages.recipient_id, " +
                "recipientTable.male AS recipientMale, " +

                "posted_messages.retweeted, " +
                "posted_messages.posted_date " +

                "FROM tweagle.posted_messages " +
                "INNER JOIN tweagle.messages ON posted_messages.message_id = messages.id " +
                "INNER JOIN tweagle.donor_users oldOwnerTable ON tweagle.messages.owner_id = oldOwnerTable.du_name " +
                "INNER JOIN tweagle.donor_users oldRecipientTable ON tweagle.messages.recipient_id = oldRecipientTable.du_name " +
                "INNER JOIN tweagle.users ownerTable ON posted_messages.owner_id = ownerTable.u_name " +
                "INNER JOIN tweagle.users recipientTable ON posted_messages.recipient_id = recipientTable.u_name " +
                "WHERE posted_messages.retweeted = 0 LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(SQL, new PostedMessageRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems that no unretweeted posted message found", emptyData);
        }
    }



    // INSERTS - UPDATES
    @Override
    public void save(final PostedMessage postedMessage) throws SQLException {
        logger.debug("save posted message " + postedMessage);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, postedMessage.getMessageId());
                ps.setLong(2, postedMessage.getTwitterId());
                ps.setString(3, postedMessage.getOwner());
                if (postedMessage.getRecipient() != null) {
                    ps.setString(4, postedMessage.getRecipient());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }
                ps.setBoolean(5, postedMessage.isRetweeted());
                ps.setTimestamp(6, new Timestamp(postedMessage.getPostedDate().getTime()));
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<PostedMessage> postedMessageList) throws SQLException {
        logger.debug("save " + postedMessageList.size() + " posted messages");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PostedMessage postedMessage = postedMessageList.get(i);
                ps.setInt(1, postedMessage.getMessageId());
                ps.setLong(2, postedMessage.getTwitterId());
                ps.setString(3, postedMessage.getOwner());
                if (postedMessage.getRecipient() != null) {
                    ps.setString(4, postedMessage.getRecipient());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }
                ps.setBoolean(5, postedMessage.isRetweeted());
                ps.setTimestamp(6, new Timestamp(postedMessage.getPostedDate().getTime()));
            }

            @Override
            public int getBatchSize() {
                return postedMessageList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }


    @Override
    public void update(final PostedMessage postedMessage) throws SQLException {
        logger.info("update posted message " + postedMessage);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, postedMessage.isRetweeted());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Override
    public void update(final List<PostedMessage> postedMessageList) throws SQLException {
        logger.info("update " + postedMessageList.size() + " posted messages");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PostedMessage postedMessage = postedMessageList.get(i);
                ps.setBoolean(1, postedMessage.isRetweeted());
            }

            @Override
            public int getBatchSize() {
                return postedMessageList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }

    class PostedMessageRowMapper implements RowMapper<PostedMessage> {
        @Override
        public PostedMessage mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PostedMessage postedMessage = new PostedMessage();
            postedMessage.setId(resultSet.getInt("id"));

            postedMessage.setMessageId(resultSet.getInt("message_id"));
            postedMessage.setMessage(resultSet.getString("message"));

            postedMessage.setTwitterId(resultSet.getLong("twitter_id"));

            postedMessage.setOldOwner(resultSet.getString("oldOwner"));
            postedMessage.setOldOwnerMale(resultSet.getBoolean("oldOwnerMale"));

            postedMessage.setOldRecipient(resultSet.getString("oldRecipient"));
            if (postedMessage.getOldRecipient() != null) postedMessage.setOldRecipientMale(resultSet.getBoolean("oldRecipientMale"));

            postedMessage.setOwner(resultSet.getString("owner_id"));
            postedMessage.setOwnerMale(resultSet.getBoolean("ownerMale"));

            postedMessage.setRecipient(resultSet.getString("recipient_id"));
            if (postedMessage.getRecipient() != null) postedMessage.setRecipientMale(resultSet.getBoolean("recipientMale"));

            postedMessage.setRetweeted(resultSet.getBoolean("retweeted"));
            postedMessage.setPostedDate(resultSet.getTimestamp("posted_date"));

            return postedMessage;
        }
    }
}