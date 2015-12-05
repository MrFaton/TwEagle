package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.SynonymMapDAO;
import com.mr_faton.core.dao.WordDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.SynonymService;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.SynonymServiceReal");

    @Autowired
    private WordDAO wordDAO;
    @Autowired
    private SynonymMapDAO synonymMapDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException {
        logger.debug("begin search synonyms for word = " + word);
        List<String> synonymList = synonymMapDAO.getSynonymList(word);
        logger.debug("found " + synonymList.size() + " synonyms: " + synonymList);
        return synonymList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doWordUseful(String word) throws SQLException {
        logger.debug("begin do word useful = " + word);
        wordDAO.doWordUseful(word);
        logger.debug("end do word useful");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<String> synonymList) throws SQLException {
        logger.debug("begin save " + synonymList.size() + " synonyms: " + synonymList);
        wordDAO.save(synonymList);
        synonymMapDAO.save(synonymList);
        logger.debug("end save synonyms");
    }
}
