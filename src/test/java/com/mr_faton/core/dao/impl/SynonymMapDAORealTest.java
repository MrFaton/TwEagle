package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymMapDAO;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import util.DBTestHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 04.12.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
@Transactional
public class SynonymMapDAORealTest {
    private static final String TABLE = "synonym_map";
    private static final String COMMON_DATA_SET = "/data_set/synonym_map/common.xml";
    private static final String EMPTY_TABLE = "/data_set/synonym_map/empty.xml";

    @Autowired
    SynonymMapDAO synonymMapDAO;
    @Autowired
    DBTestHelper dbTestHelper;

    @Before
    public void setUp() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }

    @Test
    public void getSynonymList() throws Exception {
        List<String> synonymList = synonymMapDAO.getSynonymList("хорошо");
        Assert.assertTrue(synonymList.size() == 2);
        Assert.assertTrue(synonymList.contains("замечательно") && synonymList.contains("прекрастно"));
    }

    @Test
    public void save() throws Exception {
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSave = "/data_set/synonym_map/afterSave.xml";

        synonymMapDAO.save(Arrays.asList("милый", "великолепный", "чувствительный"));

        ITable expected = dbTestHelper.getTableFromFile(TABLE, afterSave);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
