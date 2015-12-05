package com.mr_faton.core.service;

import com.mr_faton.core.exception.NoSuchEntityException;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public interface SynonymService {
    List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException;
    void doWordUseful(String word) throws SQLException;

    void save(List<String> synonymList) throws SQLException;
}
