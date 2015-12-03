package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.WordDAO;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import util.DBTestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 03.12.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
@Transactional
public class WordDAORealTest {
    private static final String TABLE = "words";
    private static final String COMMON_DATA_SET = "/data_set/word/common.xml";
    private static final String EMPTY_TABLE = "/data_set/word/empty.xml";

    @Autowired
    private WordDAO wordDAO;
    @Autowired
    private DBTestHelper dbTestHelper;

    @Test
    public void doWordUseful() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
        String afterDoWordUseful = "/data_set/word/afterDoWordUseful.xml";

        wordDAO.doWordUseful("ночь");

        ITable expected = dbTestHelper.getTableFromFile(TABLE, afterDoWordUseful);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }

    @Test
    public void save() throws Exception {
        dbTestHelper.fill(EMPTY_TABLE);
        String afterSaveList = "/data_set/word/afterSaveList.xml";
        List<String> wordList = new ArrayList<>(2);
        wordList.add("good");
        wordList.add("nice");

        wordDAO.save(wordList);

        ITable expected = dbTestHelper.getTableFromFile(TABLE, afterSaveList);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
