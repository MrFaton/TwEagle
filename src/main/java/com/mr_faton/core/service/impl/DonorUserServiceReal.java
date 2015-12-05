package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.DonorUserService;
import com.mr_faton.core.table.DonorUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 12.11.2015
 */
public class DonorUserServiceReal implements DonorUserService {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.DonorUserServiceReal");
    @Autowired
    DonorUserDAO donorUserDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException {
        logger.debug("begin search donor for parse messages");
        DonorUser donorForMessage = donorUserDAO.getDonorForMessage();
        logger.debug("end search donor for parse messages");
        return donorForMessage;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(DonorUser donorUser) throws SQLException {
        logger.info("begin save ");
        donorUserDAO.save(donorUser);
        logger.info("end save");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<DonorUser> donorUserList) throws SQLException {
        logger.info("begin save " + donorUserList.size() + " donor users ");
        donorUserDAO.save(donorUserList);
        logger.info("end save list");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DonorUser donorUser) throws SQLException {
        logger.info("begin update " + donorUser);
        donorUserDAO.update(donorUser);
        logger.info("end update");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<DonorUser> donorUserList) throws SQLException {
        logger.info("begin update " + donorUserList.size() + " donor users");
        donorUserDAO.update(donorUserList);
        logger.info("end update list");
    }
}
