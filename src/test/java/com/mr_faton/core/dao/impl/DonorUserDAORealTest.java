package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.util.TimeWizard;
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
import static org.junit.Assert.fail;

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
    private static final long DAY_IN_MS = 86400000;


    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE du_name LIKE 'DonorUserDAOReal%';";
        JDBC_TEMPLATE.update(SQL);
    }


    @Test
    public void getDonorForMessage() throws Exception {
        DonorUser donorUser = createDefaultDonorUser();
        donorUser.setTakeMessageDate(null);
        DONOR_USER_DAO.save(donorUser);
        DONOR_USER_DAO.getDonorForMessage();
    }




    @Test
    public void saveAndUpdate() throws Exception {
        //Test save
        DonorUser original = createDefaultDonorUser();
        DONOR_USER_DAO.save(original);

        DonorUser extracted = getDonorUserByUniqueName(original.getName());

        equalsDonorUsers(original, extracted);

        //Test update
        original.setTakeMessageDate(new Date(System.currentTimeMillis() + DAY_IN_MS));
        original.setTakeFollowingDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 2));
        original.setTakeFollowersDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 3));

        DONOR_USER_DAO.update(original);

        extracted = getDonorUserByUniqueName(original.getName());

        equalsDonorUsers(original, extracted);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        //Test save
        DonorUser original1 = createDefaultDonorUser();
        DonorUser original2 = createDefaultDonorUser();
        List<DonorUser> donorUserList = Arrays.asList(original1, original2);

        DONOR_USER_DAO.save(donorUserList);

        DonorUser extracted1 = getDonorUserByUniqueName(original1.getName());
        DonorUser extracted2 = getDonorUserByUniqueName(original2.getName());

        equalsDonorUsers(original1, extracted1);
        equalsDonorUsers(original2, extracted2);

        //Test update
        original1.setTakeMessageDate(new Date(System.currentTimeMillis() + DAY_IN_MS));
        original1.setTakeFollowingDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 2));
        original1.setTakeFollowersDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 3));

        original2.setTakeMessageDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 4));
        original2.setTakeFollowingDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 5));
        original2.setTakeFollowersDate(new Date(System.currentTimeMillis() + DAY_IN_MS * 6));

        DONOR_USER_DAO.update(donorUserList);

        extracted1 = getDonorUserByUniqueName(original1.getName());
        extracted2 = getDonorUserByUniqueName(original2.getName());

        equalsDonorUsers(original1, extracted1);
        equalsDonorUsers(original2, extracted2);
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

    private DonorUser getDonorUserByUniqueName(String name) throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT * FROM tweagle.donor_users WHERE du_name = '" + name + "';";
        try {
            return JDBC_TEMPLATE.queryForObject(SQL, new DonorUserRowMapper());
        } catch (EmptyResultDataAccessException emptyData) {
            throw new NoSuchEntityException("it's seems that donor user not found by unique name " + name, emptyData);
        }
    }

    private void equalsDonorUsers(DonorUser original, DonorUser extracted) {
        assertEquals(original.getName(), extracted.getName());
        assertEquals(original.isMale(), extracted.isMale());

        if (original.getTakeMessageDate() != null) {
            if (extracted.getTakeMessageDate() != null) {
                String formattedOriginal = TimeWizard.formatDate(original.getTakeMessageDate().getTime());
                String formattedExtracted = TimeWizard.formatDate(extracted.getTakeMessageDate().getTime());
                assertEquals(formattedOriginal, formattedExtracted);
            } else {fail();}
        } else {if (extracted.getTakeMessageDate() != null) fail();}

        if (original.getTakeFollowingDate() != null) {
            if (extracted.getTakeFollowingDate() != null) {
                String formattedOriginal = TimeWizard.formatDate(original.getTakeFollowingDate().getTime());
                String formattedExtracted = TimeWizard.formatDate(extracted.getTakeFollowingDate().getTime());
                assertEquals(formattedOriginal, formattedExtracted);
            } else {fail();}
        } else {if (extracted.getTakeFollowingDate() != null) fail();}

        if (original.getTakeFollowersDate() != null) {
            if (extracted.getTakeFollowersDate() != null) {
                String formattedOriginal = TimeWizard.formatDate(original.getTakeFollowersDate().getTime());
                String formattedExtracted = TimeWizard.formatDate(extracted.getTakeFollowersDate().getTime());
                assertEquals(formattedOriginal, formattedExtracted);
            } else {fail();}
        } else {if (extracted.getTakeFollowersDate() != null) fail();}
    }
}
