package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.NotExistsSynonymDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 20.10.2015
 */
public class NotExistsSynonymDAOReal implements NotExistsSynonymDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.impl.NotExistsSynonymDAOReal");
    private static final String SQL = "" +
            "INSERT INTO tweagle.not_exists_synonym (word) VALUES (?) ON DUPLICATE KEY UPDATE used = used + 1;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addWord(final String word) throws SQLException {
        logger.debug("add not exist synonym " + word);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, word);
            }
        };
        jdbcTemplate.update(SQL, pss);
    }
}
