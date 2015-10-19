package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.Synonym;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private static TransactionManager transactionManager;
    private static SynonymDAO synonymDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        synonymDAO = new SynonymDAOReal(transactionManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.synonyms WHERE word LIKE 'SynonymDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }


    @Test
    public void getSynonyms() throws Exception {
        final Synonym synonym = createSynonym();

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymDAO.save(synonym);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Synonym takenSynonym = synonymDAO.getSynonym(synonym.getWord());
                assertEquals(synonym.getSynonyms().get(0), takenSynonym.getSynonyms().get(0));
            }
        });
    }


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

    @Test
    public void saveAndUpdateList() throws Exception {
        final Synonym synonym1 = createSynonym();
        final Synonym synonym2 = createSynonym();
        final List<Synonym> synonymList = Arrays.asList(synonym1, synonym2);

        final String SQL1 = "" +
                "SELECT id FROM tweagle.synonyms WHERE word = '" + synonym1.getWord() + "';";
        final String SQL2 = "" +
                "SELECT id FROM tweagle.synonyms WHERE word = '" + synonym2.getWord() + "';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymDAO.save(synonymList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                Connection connection = transactionManager.getConnection();
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery(SQL1);) {
                        if (resultSet.next()) {
                            synonym1.setId(resultSet.getInt("id"));
                        } else {
                            fail();
                        }
                    }
                    try (ResultSet resultSet = statement.executeQuery(SQL2)) {
                        if (resultSet.next()) {
                            synonym2.setId(resultSet.getInt("id"));
                        } else {
                            fail();
                        }
                    }
                }
            }
        });

        synonym1.setUsed(15);
        synonym2.setUsed(20);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                synonymDAO.update(synonymList);
            }
        });
    }
}
