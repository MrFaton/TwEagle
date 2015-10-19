package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Synonym;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final String SQL_SAVE = "" +
            "INSERT INTO tweagle.synonyms (word, synonyms, used) VALUES (?, ?, ?);";
    private static final String SQL_UPDATE = "" +
            "UPDATE tweagle.synonyms SET word = ?, synonyms = ?, used = ? WHERE id = ?;";

    private final DataSource dataSource;

    public SynonymDAOReal(DataSource dataSource) {
        logger.debug("constructor");
        this.dataSource = dataSource;
    }

    @Override
    public Synonym getSynonym(String word) throws SQLException, NoSuchEntityException {
        logger.debug("get synonyms for word " + word);
        final String SQL = "" +
                "SELECT * FROM tweagle.synonyms WHERE word = ?;";
        Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, word);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createSynonym(resultSet);
                } else {
                    logger.debug("no synonyms for word " + word);
                    throw new NoSuchEntityException();
                }
            }
        }
    }

    private Synonym createSynonym(final ResultSet resultSet) throws SQLException {
        Synonym synonym = new Synonym();

        synonym.setId(resultSet.getInt("id"));
        synonym.setWord(resultSet.getString("word"));

        List<String> synonymList = new ArrayList<>();
        String rowSynonyms = resultSet.getString("synonyms");
        String[] synonymTokens = rowSynonyms.split(",");
        Collections.addAll(synonymList, synonymTokens);
        synonym.setSynonyms(synonymList);

        synonym.setUsed(resultSet.getInt("used"));

        return synonym;
    }


    // INSERTS - UPDATES
    @Override
    public void save(Synonym synonym) throws SQLException {
        logger.info("save " + synonym);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            preparedStatement.setString(1, synonym.getWord());
            preparedStatement.setString(2, synonym.getSynonymsAsString());
            preparedStatement.setInt(3, synonym.getUsed());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save(List<Synonym> synonyms) throws SQLException {
        logger.info("save " + synonyms.size() + " synonyms");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            for (Synonym synonym : synonyms) {
                preparedStatement.setString(1, synonym.getWord());
                preparedStatement.setString(2, synonym.getSynonymsAsString());
                preparedStatement.setInt(3, synonym.getUsed());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    @Override
    public void update(Synonym synonym) throws SQLException {
        logger.info("update " + synonym);
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, synonym.getWord());
            preparedStatement.setString(2, synonym.getSynonymsAsString());
            preparedStatement.setInt(3, synonym.getUsed());
            preparedStatement.setInt(4, synonym.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(List<Synonym> synonyms) throws SQLException {
        logger.info("update " + synonyms.size() + " synobyms");
        Connection connection = dataSource.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            for (Synonym synonym : synonyms) {
                preparedStatement.setString(1, synonym.getWord());
                preparedStatement.setString(2, synonym.getSynonymsAsString());
                preparedStatement.setInt(3, synonym.getUsed());
                preparedStatement.setInt(4, synonym.getId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
