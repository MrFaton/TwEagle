package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.table.DonorUser;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class DonorUserDAORealTest {
    private static final String BASE_NAME = "DonorUserDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
    private static final DonorUserDAO DONOR_USER_DAO = (DonorUserDAO) AppContext.getBeanByName("donorUserDAO");


    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE donor_name LIKE 'DonorUserDAOReal%';";
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
        DonorUser donorUser = createDefaultDonorUser();
        DONOR_USER_DAO.save(donorUser);

        donorUser.setTakeMessageDate(new Date());
        donorUser.setTakeFollowingDate(new Date());

        DONOR_USER_DAO.update(donorUser);
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
        return donorUser;
    }
}
