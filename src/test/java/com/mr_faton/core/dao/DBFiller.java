package com.mr_faton.core.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Arrays;

/**
 * Description
 *
 * @author root
 * @since 16.11.2015
 */
public class DBFiller {
    private static final String SCHEMA = "tweagle";

    public static void fill(String dataSetPath, JdbcTemplate jdbcTemplate) throws Exception {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBFiller.class.getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } finally {
            if (connection != null) connection.close();
        }
    }
    public static void generateSchemaDTD(JdbcTemplate jdbcTemplate) throws Exception{
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
            FlatDtdDataSet.write(databaseConnection.createDataSet(), new FileOutputStream("tweagle(auto-generated).dtd"));
        } finally {
            if (connection != null) connection.close();
        }
    }
}
