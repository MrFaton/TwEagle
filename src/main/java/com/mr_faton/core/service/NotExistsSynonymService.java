package com.mr_faton.core.service;

import java.sql.SQLException;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface NotExistsSynonymService {
    void addWord(String word) throws SQLException;
}
