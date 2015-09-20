package com.mr_faton.core.dao;

import com.mr_faton.core.table.PostedMessage;

import java.sql.SQLException;

/**
 * Created by Mr_Faton on 19.09.2015.
 */
public interface PostedMessageDAO {
    void savePostedMessage(PostedMessage postedMessage) throws SQLException;
}
