package com.mr_faton.core.dao;

import com.mr_faton.core.exception.NoSuchEntityException;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 12.10.2015
 */
public interface SynonymizerDAO {
    List<String> getSynonyms(String word) throws SQLException, NoSuchEntityException;
}
