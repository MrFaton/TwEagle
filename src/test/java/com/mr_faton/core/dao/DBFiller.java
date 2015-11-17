package com.mr_faton.core.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;

/**
 * Description
 *
 * @author root
 * @since 16.11.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:daoTestConfig.xml"))
public class DBFiller {
    private final JdbcTemplate jdbcTemplate;

    public DBFiller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void fill(String dataSetPath) throws Exception {
        System.out.println(jdbcTemplate);
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            System.out.println(connection);
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBFiller.class.getResourceAsStream(dataSetPath));
            System.out.println(dataSet);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } finally {
            if (connection != null) connection.close();
        }
    }
}
