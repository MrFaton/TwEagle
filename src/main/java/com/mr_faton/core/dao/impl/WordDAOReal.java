package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.WordDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 16.11.2015
 */
public class WordDAOReal implements WordDAO {
    private static final String SQL_SAVE = "" +
            "INSERT IGNORE INTO tweagle.words (word) VALUE (?);";
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void doWordUseful(String word) throws SQLException {
        final String SQL = "" +
                "UPDATE tweagle.words SET used = used + 1 WHERE word = '" + word + "';";
        jdbcTemplate.update(SQL);
    }

    @Override
    public void saveWord(final String word) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, word);
            }
        };
        jdbcTemplate.update(SQL_SAVE, pss);
    }

    @Override
    public void saveWord(final List<String> wordList) throws SQLException {
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
        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
    }
}
