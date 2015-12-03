//package com.mr_faton.core.dao.impl;
//
//import com.mr_faton.core.dao.SynonymMapDAO;
//import com.mr_faton.core.exception.NoSuchEntityException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
///**
// * Description
// *
// * @author Mr_Faton
// * @since 16.11.2015
// */
//public class SynonymMapDAOReal implements SynonymMapDAO {
//    private static final String SQL_SAVE = "" +
//            "INSERT INTO tweagle.synonym_map (word_id, synonym_id) VALUES (?, ?);";
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @Override
//    public List<String> getSynonymList(String word) throws SQLException, NoSuchEntityException {
//        final String SQL = "" +
//                "SELECT word FROM tweagle.words WHERE id IN" +
//                "(SELECT synonym_id FROM tweagle.synonym_map WHERE word_id = " +
//                "(SELECT id FROM tweagle.words WHERE word = '" + word + "'));";
//        List<String> synonymList = jdbcTemplate.queryForList(SQL, String.class);
//        if (synonymList.isEmpty()) throw new NoSuchEntityException("no synonym for word '" + word + "'");
//        return synonymList;
//    }
//
//    @Override
//    public void save(final String word, final String synonym) throws SQLException {
//        PreparedStatementSetter pss = new PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps) throws SQLException {
//                ps.setString(1, word);
//                ps.setString(2, synonym);
//            }
//        };
//        jdbcTemplate.update(SQL_SAVE, pss);
//    }
//
//    @Override
//    public void save(final List<String> wordList, final List<String> synonymList) throws SQLException {
//        if (wordList.size() != synonymList.size()) throw new SQLException("lists sizes not equals");
//        BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setString(1, wordList.get(i));
//                ps.setString(2, synonymList.get(i));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return wordList.size();
//            }
//        };
//        jdbcTemplate.batchUpdate(SQL_SAVE, bpss);
//    }
//}
