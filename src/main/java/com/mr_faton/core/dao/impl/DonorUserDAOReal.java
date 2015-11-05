package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
 * @author Mr_Faton
 * @version 1.0
 * @since 09.10.2015
 */
@Transactional(propagation = Propagation.SUPPORTS)
public class DonorUserDAOReal implements DonorUserDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.DonorUserDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT IGNORE INTO tweagle.donor_users (du_name, male, take_messages_date, " +
            "take_following_date, take_followers_date) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.donor_users SET take_messages_date = ?, take_following_date = ?, take_followers_date = ? " +
            "WHERE du_name = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException {
        logger.debug("get donorUser for parse messages");
        final String SQL = "" +
                "SELECT * FROM tweagle.donor_users WHERE take_messages_date IS NULL LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(SQL, new DonorUserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems that no user found for message", emptyData);
        }
    }

    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteUser(final String donorUserName) throws SQLException {
        logger.debug("delete donor user " + donorUserName);
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE du_name = '" + donorUserName + "';";
        jdbcTemplate.update(SQL);
    }





    // INSERTS - UPDATES
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(final DonorUser donorUser) throws SQLException {
        logger.debug("save donor user " + donorUser);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, donorUser.getName());
                ps.setBoolean(2, donorUser.isMale());

                if (donorUser.getTakeMessageDate() != null) {
                    ps.setDate(3, new Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    ps.setDate(4, new Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    ps.setDate(5, new Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
            }
        };

        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(final List<DonorUser> donorUserList) throws SQLException {
        logger.debug("save " + donorUserList.size() + " donor users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DonorUser donorUser = donorUserList.get(i);

                ps.setString(1, donorUser.getName());
                ps.setBoolean(2, donorUser.isMale());

                if (donorUser.getTakeMessageDate() != null) {
                    ps.setDate(3, new Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    ps.setDate(4, new Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    ps.setDate(5, new Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
            }

            @Override
            public int getBatchSize() {
                return donorUserList.size();
            }
        };

        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(final DonorUser donorUser) throws SQLException {
        logger.debug("update donor user " + donorUser);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (donorUser.getTakeMessageDate() != null) {
                    ps.setDate(1, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    ps.setNull(1, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    ps.setDate(2, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    ps.setNull(2, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    ps.setDate(3, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    ps.setNull(3, Types.DATE);
                }

                ps.setString(4, donorUser.getName());
            }
        };

        jdbcTemplate.update(SQL_UPDATE, pss);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(final List<DonorUser> donorUserList) throws SQLException {
        logger.debug("update " + donorUserList.size() + " donor users");
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DonorUser donorUser = donorUserList.get(i);

                if (donorUser.getTakeMessageDate() != null) {
                    ps.setDate(1, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    ps.setNull(1, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    ps.setDate(2, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    ps.setNull(2, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    ps.setDate(3, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    ps.setNull(3, Types.DATE);
                }

                ps.setString(4, donorUser.getName());
            }

            @Override
            public int getBatchSize() {
                return donorUserList.size();
            }
        };

        jdbcTemplate.batchUpdate(SQL_UPDATE, bpss);
    }
}

class DonorUserRowMapper implements RowMapper<DonorUser> {
    @Override
    public DonorUser mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(resultSet.getString("du_name"));
        donorUser.setMale(resultSet.getBoolean("male"));
        donorUser.setTakeMessageDate(resultSet.getDate("take_messages_date"));
        donorUser.setTakeFollowingDate(resultSet.getDate("take_following_date"));
        donorUser.setTakeFollowersDate(resultSet.getDate("take_followers_date"));
        return donorUser;
    }
}
