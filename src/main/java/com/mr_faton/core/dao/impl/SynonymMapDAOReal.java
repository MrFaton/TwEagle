package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymMapDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
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
 * @since 16.11.2015
 */
public class SynonymMapDAOReal implements SynonymMapDAO {
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.synonym_map (word_id, synonym_id) VALUES (" +
            "(SELECT id FROM tweagle.words WHERE word = ?), " +
            "(SELECT id FROM tweagle.words WHERE word = ?));";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException {
        final String SQL = "" +
                "SELECT word FROM tweagle.words WHERE id IN" +
                "(SELECT synonym_id FROM tweagle.synonym_map WHERE word_id = " +
                "(SELECT id FROM tweagle.words WHERE word = '" + word + "'));";
        List<String> synonymList = jdbcTemplate.queryForList(SQL, String.class);
        if (synonymList.isEmpty()) throw new NoSuchEntityException("no synonym for word '" + word + "'");
        return synonymList;
    }

    @Override
    public void save(final List<String> synonymList) throws SQLException {
//        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                String word = synonymList.get(i);
//                for (String synonym : synonymList) {
//                    if (word.equals(synonym)) continue;
//                    ps.setString(1, word);
//                    ps.setString(2, synonym);
//
//                    ps.addBatch();
//                    System.out.println(word + " = " + synonym);
//                }
//            }
//
//            @Override
//            public int getBatchSize() {
//                return synonymList.size();
//            }
//        };
//
//        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
        List<Object[]> batchArgs = new ArrayList<>();
        for (String word : synonymList) {
            for (String synonym : synonymList) {
                if (word.equals(synonym)) continue;
                System.out.println(word + " = " + synonym);
                String[] args = {word, synonym};
                batchArgs.add(args);
            }
        }
        jdbcTemplate.batchUpdate(SQL_SAVE, batchArgs);
    }

}
