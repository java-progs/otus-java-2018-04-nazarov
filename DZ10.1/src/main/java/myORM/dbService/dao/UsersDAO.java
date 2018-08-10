package myORM.dbService.dao;

import myORM.base.dataSets.DataSet;
import myORM.base.dataSets.reflections.ObjectField;
import myORM.base.dataSets.reflections.TableColumn;
import myORM.dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import static myORM.base.dataSets.reflections.DataSetReflection.*;

public class UsersDAO {

    private final Connection connection;

    public UsersDAO(Connection connection) {
        this.connection = connection;
    }

    public String getMetaData() {
        try {
            return "Connected to " + connection.getMetaData().getURL() + "\n" +
                    "DB product name " + connection.getMetaData().getDatabaseProductName() + "\n" +
                    "DB user " + connection.getMetaData().getUserName() + "\n" +
                    "DB version " + connection.getMetaData().getDatabaseMajorVersion() + "\n" +
                    "DB driver " + connection.getMetaData().getDriverName() + "\n";
        } catch (SQLException se) {
            se.printStackTrace();
            return se.getMessage();
        }
    }

    public void prepareDB(Class<?> clazz) {
        final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS %s (%s)";

        String tableName = getTableName(clazz);
        if (tableName.equals("")) throw new RuntimeException("No entity class " + clazz.getSimpleName());
        System.out.println("PrepareDB. TableMyORM name: " + tableName);

        Set<TableColumn> tableColumn = getDataSetColumns(clazz);

        StringBuilder columns = new StringBuilder();
        StringBuilder primaryKeys = new StringBuilder();

        for (TableColumn column : tableColumn) {

            columns.append(column.getName());
            columns.append(" ");
            columns.append(column.getDataType());
            if (column.getColumnType().equals(IS_ID_KEY)) {
                primaryKeys.append(column.getName() + ",");
                columns.append(" AUTO_INCREMENT");
            }
            columns.append(",");
        }

        if (columns.length() > 0) {
            columns.deleteCharAt(columns.length() - 1);
        }

        if (primaryKeys.length() > 0) {
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
            columns.append(", PRIMARY KEY(" + primaryKeys + ")");
        }

        String query = String.format(CREATE_SQL, tableName, columns);

        updateQuery(query);
    }

    public void deleteTable(Class<?> clazz) {
        final String DELETE_SQL = "DROP TABLE %s";

        String tableName = getTableName(clazz);
        if (tableName.equals("")) throw new RuntimeException("No entity class " + clazz.getSimpleName());

        String query = String.format(DELETE_SQL, tableName);


        updateQuery(query);
    }

    public long insertDataSetObject(DataSet obj) {
        Set<ObjectField> fields = getDataSetFields(obj);

        StringBuilder fieldsNames = new StringBuilder();
        StringBuilder fieldsValues = new StringBuilder();

        for (ObjectField field : fields) {
            fieldsNames.append(field.getName() + ",");
            fieldsValues.append(prepareValue(field.getObject()));
        }

        if (fieldsNames.length() > 0) {
            fieldsNames.deleteCharAt(fieldsNames.length() - 1);
        }

        if (fieldsValues.length() > 0) {
            fieldsValues.deleteCharAt(fieldsValues.length() - 1);
        }

        String query = "INSERT INTO " + getTableName(obj.getClass()) +" (" + fieldsNames + ") VALUES (" + fieldsValues + ")";

        long id = insertQuery(query);
        obj.setId(id);

        saveOneToManyMatch(obj);

        return id;

    }

    public <T extends DataSet> T readDataSet(long id, Class<?> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE id = " + id;
        return selectObjectQuery(query, clazz);
    }

    public <T extends DataSet> T readDataSet(String name, Class<T> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE name = " + name + " LIMIT 0,1";

        return selectObjectQuery(query, clazz);
    }

    public <T extends DataSet> List<T> readAllDataSet(Class<T> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz);

        Executor exec = new Executor(connection);
        return exec.selectAllObjectsQuery(query, clazz);
    }

    public <T extends DataSet> List<T> readAllDataSet(String fieldName, String fieldValue, Class<?> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE " + fieldName + " = ?";

        Executor exec = new Executor(connection);
        return exec.selectAllObjectsQueryByField(query, fieldValue, clazz);
    }

    private int updateQuery(String query) {
        Executor exec = new Executor(connection);
        return exec.updateQuery(query);
    }

    private long insertQuery(String query) {
        Executor exec = new Executor(connection);
        return exec.insertQuery(query);
    }

    public <T extends DataSet> T selectObjectQuery(String query, Class<?> dataSet) {
        Executor exec = new Executor(connection);
        return exec.selectObjectQuery(query, dataSet);
    }

    private String prepareValue(Object value) {
        StringBuilder queryValue = new StringBuilder();
        if (value.getClass().isInstance(new String())) {
            queryValue.append("'");
            queryValue.append(value);
            queryValue.append("'");
        } else {
            queryValue.append(value);
        }
        queryValue.append(",");

        return queryValue.toString();
    }

}
