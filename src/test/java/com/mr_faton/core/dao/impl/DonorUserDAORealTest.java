package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.util.Command;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class DonorUserDAORealTest {
    private static final String BASE_NAME = "DonorUserDAOReal";
    private static TransactionManager transactionManager;
    private static DonorUserDAO donorUserDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        donorUserDAO = new DonorUserDAOReal(transactionManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.donor_users WHERE donor_name LIKE 'DonorUserDAOReal%';";
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }


    @Test
    public void getDonorForMessage() throws Exception {
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.save(createDonorUser());
            }
        });
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.getDonorForMessage();
            }
        });
    }

    @Test
    public void deleteDonorUser() throws Exception {
        final DonorUser donorUser = createDonorUser();
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.save(donorUser);
            }
        });
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.deleteUser(donorUser);
            }
        });
    }



    @Test
    public void saveAndUpdate() throws Exception {
        final DonorUser donorUser = createDonorUser();
        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.save(donorUser);
            }
        });

        donorUser.setTakeMessageDate(new Date());
        donorUser.setTakeFollowingDate(new Date());

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.update(donorUser);
            }
        });
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final DonorUser donorUser1 = createDonorUser();
        final DonorUser donorUser2 = createDonorUser();

        final List<DonorUser> donorUserList = new ArrayList<>(5);
        donorUserList.add(donorUser1);
        donorUserList.add(donorUser2);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.save(donorUserList);
            }
        });

        donorUser1.setTakeMessageDate(new Date());
        donorUser1.setTakeFollowingDate(new Date());
        donorUser1.setTakeFollowersDate(new Date());

        donorUser2.setTakeMessageDate(new Date());
        donorUser2.setTakeFollowingDate(new Date());
        donorUser2.setTakeFollowersDate(new Date());

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                donorUserDAO.update(donorUserList);
            }
        });
    }


    private DonorUser createDonorUser() {
        DonorUser donorUser = new DonorUser();
        donorUser.setName(BASE_NAME + Counter.getNextNumber());
        donorUser.setMale(true);
        return donorUser;
    }
}
