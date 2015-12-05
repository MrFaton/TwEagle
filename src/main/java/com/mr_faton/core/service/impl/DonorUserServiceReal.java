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
        logger.debug("begin search donor for parsing messages");
        DonorUser donorForMessage = donorUserDAO.getDonorForMessage();
        logger.debug("end search donor for parsing messages");
        return donorForMessage;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(DonorUser donorUser) throws SQLException {
        logger.debug("begin save " + donorUser);
        donorUserDAO.save(donorUser);
        logger.debug("end save");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<DonorUser> donorUserList) throws SQLException {
        logger.debug("begin save " + donorUserList.size() + " donor users: " + donorUserList);
        donorUserDAO.save(donorUserList);
        logger.debug("end save list");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DonorUser donorUser) throws SQLException {
        logger.debug("begin update " + donorUser);
        donorUserDAO.update(donorUser);
        logger.debug("end update");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<DonorUser> donorUserList) throws SQLException {
        logger.debug("begin update " + donorUserList.size() + " donor users: " + donorUserList);
        donorUserDAO.update(donorUserList);
        logger.debug("end update list");
    }
}
