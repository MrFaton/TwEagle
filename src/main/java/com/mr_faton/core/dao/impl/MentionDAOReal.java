package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MentionDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Mention;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 30.11.2015
 */
public class MentionDAOReal implements MentionDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.MentionDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.mentions (owner_id, recipient_id, message, posted_date, synonymized, reposted) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.mentions SET message = ?, synonymized = ?, reposted = ? WHERE id = ?;";
    private static final String SQL_SELECT = "" +
            "SELECT id, message, posted_date, synonymized, reposted, " +
            "owner.du_name AS owner_name, owner.male AS owner_male, " +
            "recipient.du_name AS recipient_name, recipient.male AS recipient_male " +
            "FROM tweagle.mentions " +
            "INNER JOIN tweagle.donor_users owner ON mentions.owner_id = owner.du_name " +
            "INNER JOIN tweagle.donor_users recipient ON mentions.recipient_id = recipient.du_name ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Mention getMention(final boolean ownerMale, final boolean recipientMale)
            throws SQLException, NoSuchEntityException {
        final String PREDICATE = "" +
                "WHERE synonymized = 1 AND reposted = 0 AND owner_male = ? AND recipient_male = ? LIMIT 1;";
        final String SQL = SQL_SELECT + PREDICATE;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, ownerMale);
                ps.setBoolean(2, recipientMale);
            }
        };
        List<Mention> query = jdbcTemplate.query(SQL, pss, new MentionRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("" +
                    "no mention found with parameters: " +
                    "owner male = '" + ownerMale + "', " +
                    "recipient male = '" + recipientMale + "'");
        }
        return query.get(0);
    }

    @Override
    public Mention getMention(final boolean ownerMale, final boolean recipientMale, final Date minDate, final Date maxDate)
            throws SQLException, NoSuchEntityException {
        final String PREDICATE = "" +
                "WHERE synonymized = 1 AND reposted = 0 AND owner_male = ? AND recipient_male = ? AND " +
                "posted_date BETWEEN ? AND ? LIMIT 1;";
        final String SQL = SQL_SELECT + PREDICATE;
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, ownerMale);
                ps.setBoolean(2, recipientMale);
                ps.setTimestamp(3, new Timestamp(minDate.getTime()));
                ps.setTimestamp(4, new Timestamp(maxDate.getTime()));
            }
        };
        List<Mention> query = jdbcTemplate.query(SQL, pss, new MentionRowMapper());
        if (query.isEmpty()) {
            throw new NoSuchEntityException("no mention found with parameters: " +
                    "owner male = '" + ownerMale + "', " +
                    "recipient male = '" + recipientMale + "', " +
                    "between dates '" + TimeWizard.formatDateWithTime(minDate.getTime()) + "' " +
                    "and '" + TimeWizard.formatDateWithTime(maxDate.getTime()) + "'");
        }
        return query.get(0);
    }

    @Override
    public void save(final Mention mention) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, mention.getOwner());
                ps.setString(2, mention.getRecipient());
                ps.setString(3, mention.getMessage());
                ps.setTimestamp(4, new Timestamp(mention.getPostedDate().getTime()));
                ps.setBoolean(5, mention.isSynonymized());
                ps.setBoolean(6, mention.isReposted());
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void save(final List<Mention> mentionList) throws SQLException {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Mention mention = mentionList.get(i);
                ps.setString(1, mention.getOwner());
                ps.setString(2, mention.getRecipient());
                ps.setString(3, mention.getMessage());
                ps.setTimestamp(4, new Timestamp(mention.getPostedDate().getTime()));
                ps.setBoolean(5, mention.isSynonymized());
                ps.setBoolean(6, mention.isReposted());
            }

            @Override
            public int getBatchSize() {
                return mentionList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }

    @Override
    public void update(final Mention mention) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, mention.getMessage());
                ps.setBoolean(2, mention.isSynonymized());
                ps.setBoolean(3, mention.isReposted());
                ps.setInt(4, mention.getId());
            }
        };
        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Override
    public void update(final List<Mention> mentionList) throws SQLException {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Mention mention = mentionList.get(0);
                ps.setString(1, mention.getMessage());
                ps.setBoolean(2, mention.isSynonymized());
                ps.setBoolean(3, mention.isReposted());
                ps.setInt(4, mention.getId());
            }

            @Override
            public int getBatchSize() {
                return mentionList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }

    class MentionRowMapper implements RowMapper<Mention> {
        @Override
        public Mention mapRow(ResultSet resultSet, int i) throws SQLException {
            Mention mention = new Mention();

            mention.setId(resultSet.getInt("id"));
            mention.setMessage(resultSet.getString("message"));
            mention.setPostedDate(resultSet.getTimestamp("posted_date"));
            mention.setSynonymized(resultSet.getBoolean("synonymized"));
            mention.setReposted(resultSet.getBoolean("reposted"));
            mention.setOwner(resultSet.getString("owner_name"));
            mention.setOwnerMale(resultSet.getBoolean("owner_male"));
            mention.setRecipient(resultSet.getString("recipient_name"));
            mention.setRecipientMale(resultSet.getBoolean("recipient_male"));

            return mention;
        }
    }
}
