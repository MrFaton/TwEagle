package com.mr_faton.core.dao.impl;


import util.DBTestHelper;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;


/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
public class DonorUserDAORealTest {
    private static final String TABLE = "donor_users";
    private static final String COMMON_DATA_SET = "/data_set/donor_user/common.xml";
    private static final String EMPTY_TABLE = "/data_set/donor_user/empty.xml";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Autowired
    private DonorUserDAO donorUserDAO;
    @Autowired
    private DBTestHelper dbTestHelper;

    @Before
    public void before() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }


    @Test
    public void getDonorForMessage() throws Exception {
        DonorUser donorUser = donorUserDAO.getDonorForMessage();
        Assert.assertNull(donorUser.getTakeMessageDate());
    }

    @Test
    public void saveAndUpdate() throws Exception{
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSave = "/data_set/donor_user/afterSave.xml";
        String afterUpdate = "/data_set/donor_user/afterUpdate.xml";

        DonorUser donorUser = new DonorUser();
        donorUser.setName("Alex");
        donorUser.setMale(true);
        donorUser.setTakeMessageDate(TimeWizard.stringToDate("2013-12-26", DATE_PATTERN));
        donorUser.setTakeFollowingDate(TimeWizard.stringToDate("2014-08-09", DATE_PATTERN));
        donorUser.setTakeFollowersDate(TimeWizard.stringToDate("2015-10-15", DATE_PATTERN));

        ITable expected;
        ITable actual;

        //Test save
        donorUserDAO.save(donorUser);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSave);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);

        //Test update
        donorUser.setTakeMessageDate(TimeWizard.stringToDate("2012-01-02", DATE_PATTERN));
        donorUser.setTakeFollowingDate(TimeWizard.stringToDate("2013-03-04", DATE_PATTERN));
        donorUser.setTakeFollowersDate(TimeWizard.stringToDate("2014-05-06", DATE_PATTERN));
        donorUserDAO.update(donorUser);
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSaveList = "/data_set/donor_user/afterSaveList.xml";
        String afterUpdateList = "/data_set/donor_user/afterUpdateList.xml";

        DonorUser du1 = new DonorUser();
        du1.setName("Andy");
        du1.setMale(true);
        du1.setTakeMessageDate(TimeWizard.stringToDate("2010-01-02", DATE_PATTERN));
        du1.setTakeFollowingDate(TimeWizard.stringToDate("2011-02-03", DATE_PATTERN));
        du1.setTakeFollowersDate(TimeWizard.stringToDate("2012-03-04", DATE_PATTERN));

        DonorUser du2 = new DonorUser();
        du2.setName("Polly");
        du2.setMale(false);
        du2.setTakeMessageDate(TimeWizard.stringToDate("2000-01-02", DATE_PATTERN));
        du2.setTakeFollowingDate(TimeWizard.stringToDate("2001-02-03", DATE_PATTERN));
        du2.setTakeFollowersDate(TimeWizard.stringToDate("2002-03-04", DATE_PATTERN));

        List<DonorUser> duList = Arrays.asList(du1, du2);

        ITable expected;
        ITable actual;

        //Test save
        donorUserDAO.save(duList);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSaveList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);

        //Test update
        du1.setTakeMessageDate(TimeWizard.stringToDate("2015-01-02", DATE_PATTERN));
        du1.setTakeFollowingDate(TimeWizard.stringToDate("2016-02-03", DATE_PATTERN));
        du1.setTakeFollowersDate(TimeWizard.stringToDate("2017-03-04", DATE_PATTERN));

        du2.setTakeMessageDate(TimeWizard.stringToDate("2005-01-02", DATE_PATTERN));
        du2.setTakeFollowingDate(TimeWizard.stringToDate("2006-02-03", DATE_PATTERN));
        du2.setTakeFollowersDate(TimeWizard.stringToDate("2007-03-04", DATE_PATTERN));

        donorUserDAO.update(duList);

        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdateList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);
    }

    private void equalsDonorUsers(DonorUser expected, DonorUser actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isMale(), actual.isMale());

        Assert.assertEquals(expected.getTakeMessageDate(), expected.getTakeMessageDate());
        Assert.assertEquals(expected.getTakeFollowingDate(), expected.getTakeFollowingDate());
        Assert.assertEquals(expected.getTakeFollowersDate(), expected.getTakeFollowersDate());
    }
}
