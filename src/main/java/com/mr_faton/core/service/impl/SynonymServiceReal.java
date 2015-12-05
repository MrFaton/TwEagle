package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.SynonymMapDAO;
import com.mr_faton.core.dao.WordDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public class SynonymServiceReal implements SynonymService{
    @Autowired
    private WordDAO wordDAO;
    @Autowired
    private SynonymMapDAO synonymMapDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException {
        return synonymMapDAO.getSynonymList(word);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doWordUseful(String word) throws SQLException {
        wordDAO.doWordUseful(word);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<String> synonymList) throws SQLException {
        wordDAO.save(synonymList);
        synonymMapDAO.save(synonymList);
    }
}
