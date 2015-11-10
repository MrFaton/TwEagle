package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Synonym;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

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
public class SynonymDAOReal implements SynonymDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.SynonymDAOReal");
    private static final String SQL_SAVE_WORDS = "" +
            "INSERT IGNORE INTO tweagle.words (word) VALUES (?);";
    private static final String SQL_SAVE_SYNONYM_MAP = "" +
            "INSERT IGNORE tweagle (word_id, synonym_id) VALUES (?, ?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Synonym getSynonym(String word) throws SQLException, NoSuchEntityException {
        logger.debug("get synonyms for word " + word);
//        final String SQL = "" +
//                "SELECT * FROM tweagle.synonyms WHERE word = ?;";
//        Connection connection = dataSource.getConnection();
//        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setString(1, word);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    return createSynonym(resultSet);
//                } else {
//                    logger.debug("no synonyms for word " + word);
//                    throw new NoSuchEntityException();
//                }
//            }
//        }
        return null;
    }

    @Override
    public void doWordUseful(String word) throws SQLException {

    }

    // INSERTS - UPDATES
    @Override
    public void save(Synonym synonym) throws SQLException {
        logger.info("save " + synonym);
        List<String> synonymsAsWords = new ArrayList<>();
        synonymsAsWords.add(synonym.getWord());
        synonymsAsWords.addAll(synonym.getSynonyms());

        saveWords(synonymsAsWords);

    }

    @Override
    public void save(List<Synonym> synonyms) throws SQLException {
//        logger.info("save " + synonyms.size() + " synonyms");
//        Connection connection = dataSource.getConnection();
//        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
//            for (Synonym synonym : synonyms) {
//                preparedStatement.setString(1, synonym.getWord());
//                preparedStatement.setString(2, synonym.getSynonymsAsString());
//                preparedStatement.setInt(3, synonym.getUsed());
//
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        }
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

}
