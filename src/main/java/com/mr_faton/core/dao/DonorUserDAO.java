package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;

import java.sql.SQLException;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 07.10.2015
 */
public interface DonorUserDAO {
    DonorUser getDonorForMessage() throws SQLException, NoSuchEntityException;
}
