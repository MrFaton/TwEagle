package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.PostedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public class PostedMessageServiceReal implements PostedMessageService {
    @Autowired
    private PostedMessageDAO postedMessageDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Long getTwitterId() throws SQLException, NoSuchEntityException {
        return postedMessageDAO.getTwitterId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(long twitterId) throws SQLException {
        postedMessageDAO.save(twitterId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<Long> twitterIdList) throws SQLException {
        postedMessageDAO.save(twitterIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(long twitterId) throws SQLException {
        postedMessageDAO.delete(twitterId);
    }
}
