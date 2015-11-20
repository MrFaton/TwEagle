package com.mr_faton.core.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileOutputStream;
import java.sql.Connection;

/**
 * Description
 *
 * @author root
 * @since 16.11.2015
 */
public class DBTestHelper {
    private static final String SCHEMA = "tweagle";

    public static void fill(String dataSetPath, JdbcTemplate jdbcTemplate) throws Exception {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } finally {
            if (connection != null) connection.close();
        }
    }

    public static IDataSet getDataSetFromSchema(JdbcTemplate jdbcTemplate) throws Exception {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
            return databaseConnection.createDataSet();
        } finally {
            if (connection != null) connection.close();
        }
    }
    public static ITable getTableFromSchema(String tableName, JdbcTemplate jdbcTemplate) throws Exception{
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
            IDataSet dataSet = databaseConnection.createDataSet();
            return dataSet.getTable(tableName);
        } finally {
            if (connection != null) connection.close();
        }
    }

    public static IDataSet getDataSetFromFile(String xmlFilePath) throws Exception {
        return new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(xmlFilePath));
    }
    public static ITable getTableFromFile(String tableName, String xmlFilePath) throws Exception {
        FlatXmlDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(xmlFilePath));
        return dataSet.getTable(tableName);
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
