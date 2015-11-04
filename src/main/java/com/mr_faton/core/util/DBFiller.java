package com.mr_faton.core.util;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.FullXmlDataFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 04.11.2015
 */
public class DBFiller {
    @Autowired
    @Qualifier(value = "dataSourceTest")
    private DataSource dataSource;

    public void fill(String xmlDataFileName) throws DatabaseUnitException, SQLException {
        Connection connect= null;
        try {
            connect = DataSourceUtils.getConnection(dataSource);
            IDatabaseConnection dbUnitConnect = new DatabaseConnection(connect);
            FullXmlDataFileLoader loader = new FullXmlDataFileLoader();
            IDataSet xmlDataSet = loader.load(xmlDataFileName);
            System.out.println(Arrays.toString(xmlDataSet.getTableNames()));

            DatabaseOperation insert = DatabaseOperation.INSERT;
            insert.execute(dbUnitConnect, xmlDataSet);
        } finally {
            DataSourceUtils.releaseConnection(connect, dataSource);
        }
    }
}
