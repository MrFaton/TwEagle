package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface UserDAO {
    User getUserByName(String name) throws SQLException, NoSuchEntityException;
    List<User> getUserList() throws SQLException, NoSuchEntityException;

    void updateUser(User user) throws SQLException;
    void updateUserList(List<User> userList) throws SQLException;

    void addUser(User user) throws SQLException;
}
