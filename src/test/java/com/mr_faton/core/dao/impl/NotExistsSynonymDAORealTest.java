package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.NotExistsSynonymDAO;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
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
 * @since 11.11.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
@Transactional
public class NotExistsSynonymDAORealTest {
    private static final String TABLE = "not_exists_synonym";

    @Autowired
    private NotExistsSynonymDAO notExistsSynonymDAO;
    @Autowired
    DBTestHelper dbTestHelper;

    @Test
    public void addWord() throws Exception {
        String afterAddWord = "/data_set/not_exists_synonym/afterAddWord.xml";
        String word = "man";

        notExistsSynonymDAO.addWord(word);
        notExistsSynonymDAO.addWord(word);
        notExistsSynonymDAO.addWord(word);

        ITable expected = dbTestHelper.getTableFromFile(TABLE, afterAddWord);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE);

        Assertion.assertEqualsIgnoreCols(expected, actual, DBTestHelper.IGNORED_COLUMN_ID);
    }
}
