package util;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.sql.Connection;

/**
 * Description
 *
 * @author root
 * @since 16.11.2015
 */
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
public class DBTestHelper {
    public static final String[] IGNORED_COLUMN_ID = {"id"};
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private DataSource dataSource;

    public void fill(String dataSetPath) throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(dataSetPath));
        DatabaseOperation.CLEAN_INSERT.execute(getDatabaseConnection(), dataSet);
    }

    public IDataSet getDataSetFromSchema() throws Exception {
        return getDatabaseConnection().createDataSet();
    }

    public ITable getTableFromSchema(String tableName) throws Exception{
        IDataSet dataSet = getDatabaseConnection().createDataSet();
        return dataSet.getTable(tableName);
    }

    public IDataSet getDataSetFromFile(String xmlFilePath) throws Exception {
        return new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(xmlFilePath));
    }
    public ITable getTableFromFile(String tableName, String xmlFilePath) throws Exception {
        FlatXmlDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(xmlFilePath));
        return dataSet.getTable(tableName);
    }

    public int getId(String tableName, int row) throws Exception {
        ITable table = getTableFromSchema(tableName);
        return Integer.valueOf(table.getValue(row, "id").toString());
    }

    public void generateSchemaDTD() throws Exception{
        FlatDtdDataSet.write(getDatabaseConnection().createDataSet(),
                new FileOutputStream("tweagle(auto-generated).dtd"));
    }

    private IDatabaseConnection getDatabaseConnection() throws DatabaseUnitException {
        final String SCHEMA = "tweagle";
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseConnection databaseConnection = new DatabaseConnection(connection, SCHEMA);
        DatabaseConfig databaseConfig = databaseConnection.getConfig();
        databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
        return databaseConnection;
    }
}
