package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.UserService;
import com.mr_faton.core.table.User;
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
public class UserServiceReal implements UserService {
    @Autowired
    UserDAO userDAO;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User getUserByName(String name) throws SQLException, NoSuchEntityException {
        return userDAO.getUserByName(name);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> getUserList() throws SQLException, NoSuchEntityException {
        return userDAO.getUserList();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(User user) throws SQLException {
        userDAO.save(user);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<User> userList) throws SQLException {
        userDAO.save(userList);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(User user) throws SQLException {
        userDAO.update(user);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(List<User> userList) throws SQLException {
        userDAO.update(userList);
    }
}
