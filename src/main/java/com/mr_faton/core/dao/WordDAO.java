package com.mr_faton.core.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface WordDAO {
    void doWordUseful(String word) throws SQLException;

    void save(List<String> wordList) throws SQLException;
}
