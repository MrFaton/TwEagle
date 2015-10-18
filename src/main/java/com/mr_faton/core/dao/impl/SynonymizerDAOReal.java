package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 15.10.2015
 */
public class SynonymizerDAOReal implements SynonymizerDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.SynonymizerDAOReal");

    private final DataSource dataSource;

    public SynonymizerDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getSynonyms(String word) throws SQLException, NoSuchEntityException {
        logger.debug("get synonyms for word " + word);
        final String SQL = "" +
                "SELECT synonyms FROM tweagle.synonyms WHERE word = '" + word + "';";
        Connection connection = dataSource.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL)) {
            if (resultSet.next()) {
                List<String> synonymList = new ArrayList<>();

                String rowSynonyms = resultSet.getString("synonyms");
                String[] synonymTokens = rowSynonyms.split(",");
                Collections.addAll(synonymList, synonymTokens);

                return synonymList;
            } else {
                logger.debug("no synonyms for word " + word);
                throw new NoSuchEntityException();
            }
        }
    }

    @Override
    public void addWords(Map<String, String> wordsMap) throws SQLException {
        logger.debug("add " + wordsMap.size() + " to db");
        final String SQL = "" +
                "INSERT INTO tweagle.synonyms (word, synonyms) VALUES (?, ?);";
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            for (Map.Entry<String, String> entry : wordsMap.entrySet()) {
                preparedStatement.setString(1, entry.getKey());
                preparedStatement.setString(2, entry.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
