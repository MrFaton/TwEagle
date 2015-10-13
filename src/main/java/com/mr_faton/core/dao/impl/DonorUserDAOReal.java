package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

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
            "INSERT INTO tweagle.donor_users (donor_name, is_male, take_messages, take_following, take_followers, " +
            "take_messages_date, take_following_date, take_followers_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.donor_users SET take_messages = ?, take_following = ?, take_followers = ?, " +
            "take_messages_date = ?, take_following_date = ?, take_followers_date = ? WHERE donor_name = ?;";

    private final DataSource dataSource;

    public DonorUserDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException {
        logger.debug("get donorUser for parse messages");
        final String SQL = "" +
                "SELECT * FROM tweagle.donor_users WHERE take_messages = 0 LIMIT 1;";
        Connection connection = dataSource.getConnection();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) return getDonorUser(resultSet);
            throw new NoSuchEntityException("no donor to parse messages");
        }
    }


    private DonorUser getDonorUser(final ResultSet resultSet) throws SQLException {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(resultSet.getString("donor_name"));
        donorUser.setMale(resultSet.getBoolean("is_male"));
        donorUser.setTakeMessage(resultSet.getBoolean("take_messages"));
        donorUser.setTakeFollowing(resultSet.getBoolean("take_following"));
        donorUser.setTakeFollowers(resultSet.getBoolean("take_followers"));
        donorUser.setTakeMessageDate(resultSet.getDate("take_messages_date"));
        donorUser.setTakeFollowingDate(resultSet.getDate("take_following_date"));
        donorUser.setTakeFollowersDate(resultSet.getDate("take_followers_date"));
        return donorUser;
    }




    // INSERTS - UPDATES

    @Override
    public void save(DonorUser donorUser) throws SQLException {
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, donorUser.getName());
            preparedStatement.setBoolean(2, donorUser.isMale());

            preparedStatement.setBoolean(3, donorUser.isTakeMessage());
            preparedStatement.setBoolean(4, donorUser.isTakeFollowing());
            preparedStatement.setBoolean(5, donorUser.isTakeFollowers());

            if (donorUser.getTakeMessageDate() != null) {
                preparedStatement.setDate(6, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
            } else {
                preparedStatement.setNull(6, Types.DATE);
            }
            if (donorUser.getTakeFollowingDate() != null) {
                preparedStatement.setDate(7, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
            } else {
                preparedStatement.setNull(7, Types.DATE);
            }
            if (donorUser.getTakeFollowersDate() != null) {
                preparedStatement.setDate(8, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
            } else {
                preparedStatement.setNull(8, Types.DATE);
            }

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<DonorUser> donorUserList) throws SQLException {
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (DonorUser donorUser : donorUserList) {
                preparedStatement.setString(1, donorUser.getName());
                preparedStatement.setBoolean(2, donorUser.isMale());

                preparedStatement.setBoolean(3, donorUser.isTakeMessage());
                preparedStatement.setBoolean(4, donorUser.isTakeFollowing());
                preparedStatement.setBoolean(5, donorUser.isTakeFollowers());

                if (donorUser.getTakeMessageDate() != null) {
                    preparedStatement.setDate(6, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    preparedStatement.setNull(6, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    preparedStatement.setDate(7, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    preparedStatement.setNull(7, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    preparedStatement.setDate(8, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    preparedStatement.setNull(8, Types.DATE);
                }

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }



    @Override
    public void update(DonorUser donorUser) throws SQLException {
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setBoolean(1, donorUser.isTakeMessage());
            preparedStatement.setBoolean(2, donorUser.isTakeFollowing());
            preparedStatement.setBoolean(3, donorUser.isTakeFollowers());

            if (donorUser.getTakeMessageDate() != null) {
                preparedStatement.setDate(4, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
            } else {
                preparedStatement.setNull(4, Types.DATE);
            }
            if (donorUser.getTakeFollowingDate() != null) {
                preparedStatement.setDate(5, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
            } else {
                preparedStatement.setNull(5, Types.DATE);
            }
            if (donorUser.getTakeFollowersDate() != null) {
                preparedStatement.setDate(6, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
            } else {
                preparedStatement.setNull(6, Types.DATE);
            }

            preparedStatement.setString(7, donorUser.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<DonorUser> donorUserList) throws SQLException {
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            for (DonorUser donorUser : donorUserList) {
                preparedStatement.setBoolean(1, donorUser.isTakeMessage());
                preparedStatement.setBoolean(2, donorUser.isTakeFollowing());
                preparedStatement.setBoolean(3, donorUser.isTakeFollowers());

                if (donorUser.getTakeMessageDate() != null) {
                    preparedStatement.setDate(4, new java.sql.Date(donorUser.getTakeMessageDate().getTime()));
                } else {
                    preparedStatement.setNull(4, Types.DATE);
                }
                if (donorUser.getTakeFollowingDate() != null) {
                    preparedStatement.setDate(5, new java.sql.Date(donorUser.getTakeFollowingDate().getTime()));
                } else {
                    preparedStatement.setNull(5, Types.DATE);
                }
                if (donorUser.getTakeFollowersDate() != null) {
                    preparedStatement.setDate(6, new java.sql.Date(donorUser.getTakeFollowersDate().getTime()));
                } else {
                    preparedStatement.setNull(6, Types.DATE);
                }

                preparedStatement.setString(7, donorUser.getName());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }
}
