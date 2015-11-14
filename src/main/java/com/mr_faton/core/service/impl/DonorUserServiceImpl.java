package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.DonorUserService;
import com.mr_faton.core.table.DonorUser;
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
public class DonorUserServiceImpl implements DonorUserService {
    @Autowired
    DonorUserDAO donorUserDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException {
        return donorUserDAO.getDonorForMessage();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(DonorUser donorUser) throws SQLException {
        donorUserDAO.save(donorUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<DonorUser> donorUserList) throws SQLException {
        donorUserDAO.save(donorUserList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(DonorUser donorUser) throws SQLException {
        donorUserDAO.update(donorUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(List<DonorUser> donorUserList) throws SQLException {
        donorUserDAO.update(donorUserList);
    }
}
