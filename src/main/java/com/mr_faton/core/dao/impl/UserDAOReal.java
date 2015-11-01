package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.User;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 28.09.2015
 */
public class UserDAOReal implements UserDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.UserDAOReal");
    @PersistenceContext
    private EntityManager entityManager;

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User getUserByName(String name) throws SQLException, NoSuchEntityException {
        logger.debug("get user by name " + name);
        return entityManager.find(User.class, name);
    }

    @Override
    public List<User> getUserList() throws SQLException, NoSuchEntityException {
        logger.debug("get all users");
        return null;
    }






    // INSERTS - UPDATES
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(User user) throws SQLException {
        logger.debug("save user " + user);
        entityManager.persist(user);
    }

    @Override
    public void save(List<User> userList) throws SQLException {
        logger.debug("save " + userList.size() + " users");

    }



    @Override
    public void update(User user) throws SQLException {
        logger.debug("update user " + user);

    }

    @Override
    public void update(List<User> userList) throws SQLException {
        logger.debug("update " + userList.size() + " users");

    }
}
