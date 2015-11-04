package com.mr_faton.core.util;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.FullXmlDataFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void fill(String xmlDataFileName) throws DatabaseUnitException, SQLException {
        IDatabaseConnection iDatabaseConnection = new DatabaseConnection(dataSource.getConnection());
        FullXmlDataFileLoader loader = new FullXmlDataFileLoader();
        IDataSet iDataSet = loader.load(xmlDataFileName);



//        System.out.println(iDatabaseConnection.getSchema());
        DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, iDataSet);
    }
}
