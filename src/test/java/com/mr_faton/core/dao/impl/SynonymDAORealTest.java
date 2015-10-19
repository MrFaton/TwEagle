package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.Synonym;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    private static TransactionManager transactionManager;
    private static SynonymDAO synonymDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        synonymDAO = new SynonymDAOReal(transactionManager);
    }

//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String SQL = "" +
//                "DELETE FROM tweagle.synonyms WHERE word LIKE 'DonorUserDAOReal%';";
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                transactionManager.getConnection().createStatement().executeUpdate(SQL);
//            }
//        });
//    }

    private Synonym createSynonym() {
        Synonym synonym = new Synonym();

        synonym.setWord(BASE_NAME + Counter.getNextNumber());

        List<String> synonymList = new ArrayList<>(4);
        synonymList.add("syn1");
        synonymList.add("syn2");
        synonymList.add("syn3");
        synonym.setSynonyms(synonymList);

        synonym.setUsed(0);

        return synonym;
    }


    // INSERTS - UPDATES
    @Test
    public void saveAndUpdate() throws Exception {
        final Synonym synonym = createSynonym();
        final String SQL = "" +
                "SELECT id FROM tweagle.synonyms WHERE word = '" + synonym.getWord() + "';";


        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymDAO.save(synonym);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                ResultSet resultSet = transactionManager.getConnection().createStatement().executeQuery(SQL);
                if (resultSet.next()) {
                    synonym.setId(resultSet.getInt("id"));
                } else {
                    fail("result set must have next");
                }
            }
        });

        synonym.setUsed(10);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymDAO.update(synonym);
            }
        });
    }


//    @Test
//    public void getSynonyms() throws Exception {
//        final Map<String, String> wordMap = new HashMap<>();
//        final String word = BASE_NAME + Counter.getNextNumber();
//        wordMap.put(word, word + "+");
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                synonymDAO.addWords(wordMap);
//            }
//        });
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                assertEquals(word + "+", synonymDAO.getSynonyms(word).get(0));
//            }
//        });
//    }
}
