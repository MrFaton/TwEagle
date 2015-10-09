package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);
        if (resultSet.next()) {
            DonorUser donorUser = getDonorUser(resultSet);
            resultSet.close();
            statement.close();
            return donorUser;
        } else {
            resultSet.close();
            statement.close();
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
}
