package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;

import java.sql.SQLException;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public interface MessageDAO {
    /*TODO разрулить пол юзер на JOINе из двух таблиц*/
    Message getMessage(String userName, boolean tweet) throws SQLException, NoSuchEntityException;
    void updatePostedMessage(Message message) throws SQLException, NoSuchEntityException;
}
