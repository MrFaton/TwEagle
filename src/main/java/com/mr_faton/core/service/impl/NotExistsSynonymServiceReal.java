package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.NotExistsSynonymDAO;
import com.mr_faton.core.service.NotExistsSynonymService;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.NotExistsSynonymServiceReal");

    @Autowired
    NotExistsSynonymDAO notExistsSynonymDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addWord(String word) throws SQLException {
        logger.debug("begin save word '" + word + "'");
        notExistsSynonymDAO.addWord(word);
        logger.debug("end save word");
    }
}
