package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 09.10.2015
 */
public class DonorUserDAOReal implements DonorUserDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.DonorUserDAOReal");
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.donor_users (donor_name, is_male, take_messages_date, " +
            "take_following_date, take_followers_date) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.donor_users SET take_messages_date = ?, take_following_date = ?, take_followers_date = ? " +
            "WHERE donor_name = ?;";

    private final DataSource dataSource;

    public DonorUserDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException {
        logger.debug("get donorUser for parse messages");
        final String SQL = "" +
                "SELECT * FROM tweagle.donor_users WHERE take_messages_date IS NULL LIMIT 1;";
        Connection connection = dataSource.getConnection();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) return getDonorUser(resultSet);
            throw new NoSuchEntityException("no donor to parse messages");
        }
    }

    @Override
    public void deleteUser(DonorUser donorUser) throws SQLException {
        logger.debug("delete donor user " + donorUser);
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE donor_name = ?;";
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, donorUser.getName());
            preparedStatement.executeUpdate();
        }
    }

    private DonorUser getDonorUser(final ResultSet resultSet) throws SQLException {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(resultSet.getString("donor_name"));
        donorUser.setMale(resultSet.getBoolean("is_male"));
        donorUser.setTakeMessageDate(resultSet.getDate("take_messages_date"));
        donorUser.setTakeFollowingDate(resultSet.getDate("take_following_date"));
        donorUser.setTakeFollowersDate(resultSet.getDate("take_followers_date"));
        return donorUser;
    }




    // INSERTS - UPDATES

    @Override
    public void save(DonorUser donorUser) throws SQLException {
        logger.debug("save donor user " + donorUser);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, donorUser.getName());
            preparedStatement.setBoolean(2, donorUser.isMale());

            if (donorUser.getTakeMessageDate() != null) {
                preparedStatement.setDate(3, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }
            if (donorUser.getTakeFollowingDate() != null) {
                preparedStatement.setDate(4, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
            } else {
                preparedStatement.setNull(4, Types.DATE);
            }
            if (donorUser.getTakeFollowersDate() != null) {
                preparedStatement.setDate(5, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
            } else {
                preparedStatement.setNull(5, Types.DATE);
            }

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<DonorUser> donorUserList) throws SQLException {
        logger.debug("save " + donorUserList.size() + " donor users");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (DonorUser donorUser : donorUserList) {
                preparedStatement.setString(1, donorUser.getName());
                preparedStatement.setBoolean(2, donorUser.isMale());

                if (donorUser.getTakeMessageDate() != null) {
                    preparedStatement.setDate(3, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    preparedStatement.setNull(3, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    preparedStatement.setDate(4, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    preparedStatement.setNull(4, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    preparedStatement.setDate(5, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    preparedStatement.setNull(5, Types.DATE);
                }

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }



    @Override
    public void update(DonorUser donorUser) throws SQLException {
        logger.debug("update donor user " + donorUser);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            if (donorUser.getTakeMessageDate() != null) {
                preparedStatement.setDate(1, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
            } else {
                preparedStatement.setNull(1, Types.DATE);
            }
            if (donorUser.getTakeFollowingDate() != null) {
                preparedStatement.setDate(2, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
            } else {
                preparedStatement.setNull(2, Types.DATE);
            }
            if (donorUser.getTakeFollowersDate() != null) {
                preparedStatement.setDate(3, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }

            preparedStatement.setString(4, donorUser.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<DonorUser> donorUserList) throws SQLException {
        logger.debug("update " + donorUserList.size() + " donor users");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            for (DonorUser donorUser : donorUserList) {
                if (donorUser.getTakeMessageDate() != null) {
                    preparedStatement.setDate(1, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    preparedStatement.setNull(1, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    preparedStatement.setDate(2, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    preparedStatement.setNull(2, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    preparedStatement.setDate(3, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    preparedStatement.setNull(3, Types.DATE);
                }

                preparedStatement.setString(4, donorUser.getName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }
}
