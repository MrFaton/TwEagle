package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.NotExistsSynonymDAO;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import static org.junit.Assert.assertEquals;

/**
 * Description
 *
 * @author root
 * @since 11.11.2015
 */
public class NotExistsSynonymDAORealTest {
    private static final String BASE_NAME = "NotExistsSynonymDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
    private static final NotExistsSynonymDAO NOT_EXISTS_SYNONYM_DAO = (NotExistsSynonymDAO) AppContext.getBeanByName("notExistsSynonymDAO");

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.not_exists_synonym WHERE word LIKE 'NotExistsSynonymDAOReal%';";
        JDBC_TEMPLATE.update(SQL);
    }

    @Test
    public void addWord() throws Exception {
        String notExistsWord = BASE_NAME + Counter.getNextNumber();
        final String SQL = "" +
                "SELECT used FROM tweagle.not_exists_synonym WHERE word = '" + notExistsWord + "';";
        final int usedTimes = 4;
        for (int i = 0; i <= usedTimes ; i++) {
            NOT_EXISTS_SYNONYM_DAO.addWord(notExistsWord);
        }
        final int extractedUsedTimes = JDBC_TEMPLATE.queryForObject(SQL, Integer.class);
        assertEquals(usedTimes, extractedUsedTimes);
    }
}
