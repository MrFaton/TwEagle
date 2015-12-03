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
public interface SynonymMapDAO {
    List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException;

    void save(List<String> synonymList) throws SQLException;
}
