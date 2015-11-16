package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.PostedMessage;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface PostedMessageService {
    PostedMessage getUnRetweetedPostedMessage() throws SQLException, NoSuchEntityException;

    // INSERTS - UPDATES
    void save(PostedMessage postedMessage) throws SQLException;
    void save(List<PostedMessage> postedMessageList) throws SQLException;

    void update(PostedMessage postedMessage) throws SQLException;
    void update(List<PostedMessage> postedMessageList) throws SQLException;
}
