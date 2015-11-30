package util;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.core.JdbcTemplate;
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
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
public class DBTestHelper {
    public static final String[] IGNORED_COLUMN_ID = {"id"};
    private static final String SCHEMA = "tweagle";
    private final IDatabaseConnection databaseConnection;

    public DBTestHelper(DataSource dataSource) throws Exception {
        databaseConnection = new DatabaseConnection(dataSource.getConnection(), SCHEMA);
    }

    public void fill(String dataSetPath) throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(dataSetPath));
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
    }

    public IDataSet getDataSetFromSchema() throws Exception {
        return databaseConnection.createDataSet();
    }
    public ITable getTableFromSchema(String tableName) throws Exception{
        IDataSet dataSet = databaseConnection.createDataSet();
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
        FlatDtdDataSet.write(databaseConnection.createDataSet(), new FileOutputStream("tweagle(auto-generated).dtd"));
    }

}
