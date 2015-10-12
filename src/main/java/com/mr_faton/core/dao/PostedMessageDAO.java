package com.mr_faton.core.dao;

import com.mr_faton.core.table.PostedMessage;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mr_Faton on 19.09.2015.
 */
public interface PostedMessageDAO {

    // INSERTS - UPDATES
    void save(PostedMessage postedMessage) throws SQLException;
    void save(List<PostedMessage> postedMessageList) throws SQLException;

    void update(PostedMessage postedMessage) throws SQLException;
    void update(List<PostedMessage> postedMessageList) throws SQLException;
}
