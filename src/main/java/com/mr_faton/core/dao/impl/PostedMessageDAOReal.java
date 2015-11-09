package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.table.PostedMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
            "INSERT INTO tweagle.posted_messages (message_id, twitter_id, owner_id, recipient_id, posted_date)  " +
            "VALUES (?, ?, ?, ?, ?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // INSERTS
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                ps.setTimestamp(5, new Timestamp(postedMessage.getPostedDate().getTime()));
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                ps.setTimestamp(5, new Timestamp(postedMessage.getPostedDate().getTime()));
            }

            @Override
            public int getBatchSize() {
                return postedMessageList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }
}
class PostedMessageRowMapper implements RowMapper<PostedMessage> {
    @Override
    public PostedMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}