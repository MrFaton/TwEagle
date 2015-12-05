package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Mention;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public interface MentionService {
    Mention getMention(boolean ownerMale, boolean recipientMale) throws SQLException, NoSuchEntityException;
    Mention getMention(boolean ownerMale, boolean recipientMale, Date minDate, Date maxDate)
            throws SQLException, NoSuchEntityException;

    void save(Mention mention) throws SQLException;
    void save(List<Mention> mentionList) throws SQLException;

    void update(Mention mention) throws SQLException;
    void update(List<Mention> mentionList) throws SQLException;
}