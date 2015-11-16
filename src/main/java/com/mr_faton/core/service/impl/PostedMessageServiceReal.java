package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.PostedMessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.PostedMessageService;
import com.mr_faton.core.table.PostedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public class PostedMessageServiceReal implements PostedMessageService {
    @Autowired
    PostedMessageDAO postedMessageDAO;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PostedMessage getUnRetweetedPostedMessage() throws SQLException, NoSuchEntityException {
        return postedMessageDAO.getUnRetweetedPostedMessage();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(PostedMessage postedMessage) throws SQLException {
        postedMessageDAO.save(postedMessage);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<PostedMessage> postedMessageList) throws SQLException {
        postedMessageDAO.save(postedMessageList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(PostedMessage postedMessage) throws SQLException {
        postedMessageDAO.update(postedMessage);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(List<PostedMessage> postedMessageList) throws SQLException {
        postedMessageDAO.update(postedMessageList);
    }
}
