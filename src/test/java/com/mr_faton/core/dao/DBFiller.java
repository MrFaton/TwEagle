package com.mr_faton.core.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
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
    @Autowired
    private static DataSource dataSource;

    public static void fill(String dataSetPath) throws Exception {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBFiller.class.getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
