package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.util.Command;
import com.mr_faton.core.util.SettingsHolder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 18.10.2015
 */
public class SynonymizerDAORealTest {
    private static final String BASE_NAME = "DonorUserDAOReal";
    private static TransactionManager transactionManager;
    private static SynonymizerDAO synonymizerDAO;

    @BeforeClass
    public static void generalSetUp() throws Exception {
        SettingsHolder.loadSettings();
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        synonymizerDAO = new SynonymizerDAOReal(transactionManager);
    }


    @Test
    public void getSynonyms() throws Exception {

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                /*TODO put list with synonyms into db*/
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                System.out.println(synonymizerDAO.getSynonyms("xxxx"));
            }
        });
    }
}
