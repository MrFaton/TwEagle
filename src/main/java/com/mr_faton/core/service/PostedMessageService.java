package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public interface PostedMessageService {
    Long getTwitterId() throws SQLException, NoSuchEntityException;

    void save(long twitterId) throws SQLException;
    void save(List<Long> twitterIdList) throws SQLException;

    void delete(long twitterId) throws SQLException;
}
