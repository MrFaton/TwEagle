package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class DonorUserDAORealTest {
    private static final String BASE_NAME = "DonorUserDAOReal";
    private static final DonorUserDAO DONOR_USER_DAO = (DonorUserDAO) AppContext.getBeanByName("donorUserDAO");
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");


    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE du_name LIKE 'DonorUserDAOReal%';";
        JDBC_TEMPLATE.update(SQL);
    }


    @Test
    public void getDonorForMessage() throws Exception {
        DonorUser donorUser = createDefaultDonorUser();

        DONOR_USER_DAO.save(donorUser);
        DONOR_USER_DAO.getDonorForMessage();
    }

    @Test
    public void deleteDonorUser() throws Exception {
        DonorUser donorUser = createDefaultDonorUser();

        DONOR_USER_DAO.save(donorUser);
        DONOR_USER_DAO.deleteUser(donorUser.getName());
    }



    @Test
    public void saveAndUpdate() throws Exception {
        DonorUser originalDonorUser = createDefaultDonorUser();
        DONOR_USER_DAO.save(originalDonorUser);

        DonorUser extractedDonorUser1 = getDonorUserByUniqueName(originalDonorUser.getName());

        assertEquals(originalDonorUser, extractedDonorUser1);

        originalDonorUser.setTakeMessageDate(new Date(System.currentTimeMillis() + 1000));
        originalDonorUser.setTakeFollowingDate(new Date(System.currentTimeMillis() + 2000));
        originalDonorUser.setTakeFollowersDate(new Date(System.currentTimeMillis() + 3000));

        DONOR_USER_DAO.update(originalDonorUser);

        DonorUser extractedDonorUser2 = getDonorUserByUniqueName(originalDonorUser.getName());

        assertEquals(originalDonorUser, extractedDonorUser2);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        DonorUser donorUser1 = createDefaultDonorUser();
        DonorUser donorUser2 = createDefaultDonorUser();
        List<DonorUser> donorUserList = Arrays.asList(donorUser1, donorUser2);

        DONOR_USER_DAO.save(donorUserList);

        donorUser1.setTakeMessageDate(new Date());
        donorUser1.setTakeFollowingDate(new Date());

        donorUser2.setTakeMessageDate(new Date());
        donorUser2.setTakeFollowingDate(new Date());

        DONOR_USER_DAO.update(donorUserList);

    }


    private DonorUser createDefaultDonorUser() {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(BASE_NAME + Counter.getNextNumber());
        donorUser.setMale(true);
        donorUser.setTakeMessageDate(new Date(System.currentTimeMillis() - 1000));
        donorUser.setTakeFollowingDate(new Date(System.currentTimeMillis() - 2000));
        donorUser.setTakeFollowersDate(new Date(System.currentTimeMillis() - 3000));
        return donorUser;
    }

    DonorUser getDonorUserByUniqueName(String name) throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT * FROM tweagle.donor_users WHERE du_name = '" + name + "';";
        try {
            return JDBC_TEMPLATE.queryForObject(SQL, new DonorUserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems that donor user not found by unique name " + name, emptyData);
        }
    }
}
