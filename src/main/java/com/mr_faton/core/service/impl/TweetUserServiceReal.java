package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.TweetUserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.TweetUserService;
import com.mr_faton.core.table.TweetUser;
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
public class TweetUserServiceReal implements TweetUserService {
    @Autowired
    TweetUserDAO tweetUserDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public TweetUser getUserForTweet() throws SQLException, NoSuchEntityException {
        return tweetUserDAO.getUserForTweet();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<TweetUser> getUserList() throws SQLException, NoSuchEntityException {
        return tweetUserDAO.getUserList();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(TweetUser tweetUser) throws SQLException {
        tweetUserDAO.save(tweetUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<TweetUser> tweetUserList) throws SQLException {
        tweetUserDAO.save(tweetUserList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(TweetUser user) throws SQLException {
        tweetUserDAO.update(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(List<TweetUser> userList) throws SQLException {
        tweetUserDAO.update(userList);
    }
}
