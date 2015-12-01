package util;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.FileOutputStream;

/**
 * Description
 *
 * @author root
 * @since 16.11.2015
 */
@Transactional
@ContextConfiguration(locations = ("classpath:/daoTestConfig.xml"))
public class DBTestHelper {
    public static final String[] IGNORED_COLUMN_ID = {"id"};
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    private static final String SCHEMA = "tweagle";
    private final IDatabaseConnection databaseConnection;
    private DataSource dataSource;

    public DBTestHelper(DataSource dataSource) throws Exception {
        databaseConnection = new DatabaseConnection(dataSource.getConnection(), SCHEMA);
        this.dataSource = dataSource;
    }

    public void fill(String dataSetPath) throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(DBTestHelper.class.getResourceAsStream(dataSetPath));
        IDatabaseConnection databaseConnection2 = new DatabaseConnection(DataSourceUtils.getConnection(dataSource), SCHEMA);
        System.out.println("1 - " + databaseConnection);
        System.out.println("2 - " + databaseConnection2);
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection2, dataSet);
    }

    public IDataSet getDataSetFromSchema() throws Exception {
        return databaseConnection.createDataSet();
    }

    public ITable getTableFromSchema(String tableName) throws Exception{
        IDatabaseConnection databaseConnection2 = new DatabaseConnection(DataSourceUtils.getConnection(dataSource), SCHEMA);
//        IDataSet dataSet = new DatabaseConnection(DataSourceUtils.getConnection(dataSource)).createDataSet();
        IDataSet dataSet = databaseConnection2.createDataSet();
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
