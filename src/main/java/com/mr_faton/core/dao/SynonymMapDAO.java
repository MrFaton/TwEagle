package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface SynonymMapDAO {
    List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException;

    void save(String word, String synonym) throws SQLException;
    void save(List<String> wordList, List<String> synonymList) throws SQLException;
}
