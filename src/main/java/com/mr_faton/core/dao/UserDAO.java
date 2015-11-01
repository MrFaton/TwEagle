package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 * @version 1.0
 */
public interface UserDAO {
    User getUserByName(String name) throws SQLException, NoSuchEntityException;
    List<User> getUserList() throws SQLException, NoSuchEntityException;


    // INSERTS - UPDATES
    void save(User user) throws SQLException;
    void save(List<User> userList) throws SQLException;

    void update(User user) throws SQLException;
    void update(List<User> userList) throws SQLException;
}
