package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 15.10.2015
 */
@Transactional(propagation = Propagation.SUPPORTS)
public class SynonymDAOReal implements SynonymDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.SynonymDAOReal");
    private static final String SQL_SAVE_WORDS = "" +
            "INSERT IGNORE INTO tweagle.words (word) VALUES (?);";
    private static final String SQL_SAVE_SYNONYM_MAP = "" +
            "INSERT IGNORE tweagle.synonym_map (word_id, synonym_id) VALUES (" +
            "(SELECT id FROM tweagle.words WHERE word = ?), (SELECT id FROM tweagle.words WHERE word = ?));";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException {
        logger.debug("get synonyms for word " + word);
        final String SQL = "" +
                "SELECT word FROM tweagle.words WHERE id IN" +
                "(SELECT synonym_id FROM tweagle.synonym_map WHERE word_id = " +
                "(SELECT id FROM tweagle.words WHERE word = '" + word + "'));";
        List<String> synonymList = jdbcTemplate.queryForList(SQL, String.class);
        if (synonymList.isEmpty()) throw new NoSuchEntityException("no synonym for word '" + word + "'");
        return synonymList;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void doWordUseful(String word) throws SQLException {

    }

    // INSERT
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<String> synonymList) throws SQLException {
        logger.info("save " + synonymList.size() + " synonyms");

        saveWords(synonymList);
        saveSynonymMap(synonymList);
    }

    void saveWords(final List<String> wordList) {
        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String word = wordList.get(i);
                ps.setString(1, word);
            }

            @Override
            public int getBatchSize() {
                return wordList.size();
            }
        };
        jdbcTemplate.batchUpdate(SQL_SAVE_WORDS, bpss);
    }

    void saveSynonymMap(List<String> synonymList) {
        for (final String word : synonymList) {
            for (final String synonymWord : synonymList) {
                if (word.equals(synonymWord)) continue;
                PreparedStatementSetter pss = new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, word);
                        ps.setString(2, synonymWord);
                    }
                };
                jdbcTemplate.update(SQL_SAVE_SYNONYM_MAP, pss);
            }
        }
    }

}
