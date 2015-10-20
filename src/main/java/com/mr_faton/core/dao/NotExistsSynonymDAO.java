package com.mr_faton.core.dao;

import java.sql.SQLException;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 20.10.2015
 */
public interface NotExistsSynonymDAO {
    void addWord(String word) throws SQLException;
}
