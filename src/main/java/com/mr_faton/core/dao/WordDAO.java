package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;

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

    void saveWord(String word) throws SQLException;
    void saveWord(List<String> wordList) throws SQLException;
}
