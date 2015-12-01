package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.MentionDAO;
import com.mr_faton.core.table.Mention;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import util.DBTestHelper;

/**
 * Description
 *
 * @author root
 * @since 01.12.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
@Transactional
public class MentionDAORealTest {
    private static final String TABLE = "mentions";
    private static final String COMMON_DATA_SET = "/data_set/mention/common.xml";
    private static final String EMPTY_MENTIONS_TABLE = "/data_set/mention/empty.xml";

    @Autowired
    private MentionDAO mentionDAO;
    @Autowired
    private DBTestHelper dbTestHelper;

    @Before
    public void before() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }


    @Test
    public void saveAndUpdate() throws Exception {
        dbTestHelper.fill(EMPTY_MENTIONS_TABLE);
        String afterSave = "/data_set/mention/afterSave.xml";
        String afterUpdate = "/data_set/mention/afterUpdate.xml";

        Mention mention = new Mention();

        mention.setOwner("donorUserA");
        mention.setOwnerMale(true);
        mention.setRecipient("donorUserB");
        mention.setRecipientMale(false);
        mention.setMessage("sdfsa");
        mention.setPostedDate(TimeWizard.stringToDate("2014-03-09 15:41:26", DBTestHelper.DATE_TIME_PATTERN));
        mention.setSynonymized(true);
        mention.setReposted(false);

        ITable expected;
        ITable actual;

        //Test save
        mentionDAO.save(mention);
        expected = dbTestHelper.getTableFromFile(TABLE, afterSave);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);

        //Test update
        mention.setId(dbTestHelper.getId(TABLE, 0));
        mention.setMessage("lhml");
        mention.setSynonymized(!mention.isSynonymized());
        mention.setReposted(!mention.isReposted());

        mentionDAO.update(mention);

        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdate);
        actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
