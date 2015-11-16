package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
//import com.mr_faton.core.dao.SynonymDAO;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 18.10.2015
 */
public class SynonymDAORealTest {
    private static final String BASE_NAME = "SynonymDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
//    private static final SynonymDAO SYNONYM_DAO = (SynonymDAO) AppContext.getBeanByName("synonymDAO");

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.words WHERE word LIKE 'SynonymDAOReal%';";
        JDBC_TEMPLATE.update(SQL);
    }

    @Test
    public void saveAndGet() throws Exception {
        //Test save
        List<String> synonymList = new ArrayList<>(5);
        for (int i = 0; i < 4; i++) {
            synonymList.add(BASE_NAME + Counter.getNextNumber());
        }
//        SYNONYM_DAO.save(synonymList);

        //Test getSynonymList
        String word = synonymList.get(1);
        synonymList.remove(1);
//        List<String> extractedSynonyms = SYNONYM_DAO.getSynonymList(word);
//        if (extractedSynonyms.contains(word)) fail();
//        assertEquals(synonymList, extractedSynonyms);
    }

    @Test
    public void doWordUseful() throws Exception {
        List<String> synonymList = Arrays.asList(BASE_NAME + Counter.getNextNumber(), BASE_NAME + Counter.getNextNumber());
        final String SQL = "" +
                "SELECT used FROM tweagle.words WHERE word = '" + synonymList.get(0) + "'";
        final int usedTimes = 2;

//        SYNONYM_DAO.save(synonymList);
//        SYNONYM_DAO.doWordUseful(synonymList.get(0));
//        SYNONYM_DAO.doWordUseful(synonymList.get(0));

        final int extractedUsedTimes = JDBC_TEMPLATE.queryForObject(SQL, Integer.class);

        assertEquals(usedTimes, extractedUsedTimes);
    }
}
