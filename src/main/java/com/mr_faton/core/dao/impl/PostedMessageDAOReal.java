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
            "INSERT INTO tweagle.posted_messages (twitter_id, posted_date)  VALUES (?, ?);";
    private static final String SQL_DELETE = "" +
            "DELETE FROM tweagle.posted_messages WHERE twitter_id = ?;";
    private static final String SQL_SELECT = "" +
            "SELECT twitter_id FROM tweagle.posted_messages LIMIT 1;";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Long getTwitterId() throws SQLException, NoSuchEntityException {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT, Long.class);
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("no twitter id found", emptyData);
        }
    }

    @Override
    public void save(final long twitterId) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, twitterId);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<Long> twitterIdList) throws SQLException {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                long twitterId = twitterIdList.get(i);
                ps.setLong(1, twitterId);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }

            @Override
            public int getBatchSize() {
                return twitterIdList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }

    @Override
    public void delete(final long twitterId) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, twitterId);
            }
        };
        jdbcTemplate.update(SQL_DELETE, pss);
    }
}