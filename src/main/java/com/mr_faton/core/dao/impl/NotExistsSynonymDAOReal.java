package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.NotExistsSynonymDAO;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
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

    private final DataSource dataSource;

    public NotExistsSynonymDAOReal(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addWord(String word) throws SQLException {

    }
}
