package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Synonym;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 12.10.2015
 */
public interface SynonymDAO {
    Synonym getSynonym(String word) throws SQLException, NoSuchEntityException;
    void doWordUseful(String word) throws SQLException;

    // INSERTS - UPDATES
    void save(Synonym synonym) throws SQLException;
    void save(List<Synonym> synonyms) throws SQLException;
}
