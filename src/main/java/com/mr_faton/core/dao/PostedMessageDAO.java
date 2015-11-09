package com.mr_faton.core.dao;

import com.mr_faton.core.table.PostedMessage;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 19.09.2015
 */
public interface PostedMessageDAO {

    // INSERTS - UPDATES
    void save(PostedMessage postedMessage) throws SQLException;
    void save(List<PostedMessage> postedMessageList) throws SQLException;
}
