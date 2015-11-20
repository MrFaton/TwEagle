package com.mr_faton.core.dao.impl;


import com.mr_faton.core.table.DonorUser;
import org.junit.Assert;
import org.junit.Test;


/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class DonorUserDAORealTest {
    private static final String TABLE = "donor_users";
    private static final String EMPTY_DATA_SET = "/test/data_set/donor_user/empty.xml";

    @Test
    public void saveAndUpdate() {
        String afterSave = "";
    }

    private void equalsDonorUsers(DonorUser expected, DonorUser actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isMale(), actual.isMale());

        Assert.assertEquals(expected.getTakeMessageDate(), expected.getTakeMessageDate());
        Assert.assertEquals(expected.getTakeFollowingDate(), expected.getTakeFollowingDate());
        Assert.assertEquals(expected.getTakeFollowersDate(), expected.getTakeFollowersDate());
    }
}
