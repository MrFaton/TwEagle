package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.PostedMessageService;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.PostedMessageServiceReal");

    @Autowired
    private PostedMessageDAO postedMessageDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Long getTwitterId() throws SQLException, NoSuchEntityException {
        logger.debug("begin search posted message twitter id");
        Long twitterId = postedMessageDAO.getTwitterId();
        logger.debug("found twitter id = " + twitterId);
        return twitterId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(long twitterId) throws SQLException {
        logger.debug("begin save twitter id = " + twitterId);
        postedMessageDAO.save(twitterId);
        logger.debug("end save");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<Long> twitterIdList) throws SQLException {
        logger.debug("begin save " + twitterIdList.size() + " twitter ids: " + twitterIdList);
        postedMessageDAO.save(twitterIdList);
        logger.debug("end save list");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(long twitterId) throws SQLException {
        logger.debug("begin delete twitter id = " + twitterId);
        postedMessageDAO.delete(twitterId);
        logger.debug("end delete");
    }
}
