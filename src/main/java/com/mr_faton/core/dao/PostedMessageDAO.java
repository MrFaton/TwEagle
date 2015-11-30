package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.PostedMessage;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 19.09.2015
 */
public interface PostedMessageDAO {

    Long getTwitterId() throws SQLException, NoSuchEntityException;

    void save(long twitterId) throws SQLException;
    void save(List<Long> twitterIdList) throws SQLException;

    void delete(long twitterId) throws SQLException;
}
