package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.NotExistsSynonymDAO;
import com.mr_faton.core.service.NotExistsSynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public class NotExistsSynonymServiceReal implements NotExistsSynonymService {
    @Autowired
    NotExistsSynonymDAO notExistsSynonymDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addWord(String word) throws SQLException {
        notExistsSynonymDAO.addWord(word);
    }
}
