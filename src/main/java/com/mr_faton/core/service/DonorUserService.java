package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 12.11.2015
 */
public interface DonorUserService {
    DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException;


    void save(DonorUser donorUser) throws SQLException;
    void save(List<DonorUser> donorUserList) throws SQLException;

    void update(DonorUser donorUser) throws SQLException;
    void update(List<DonorUser> donorUserList) throws SQLException;
}
