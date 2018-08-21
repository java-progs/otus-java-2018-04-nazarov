package myORM.dbService.dao;

import myORM.base.dataSets.DataSet;
import myORM.base.dataSets.reflections.ObjectField;
import myORM.base.dataSets.reflections.TableColumn;
import myORM.cache.CacheEngine;
import myORM.cache.MyElement;
import myORM.dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static myORM.base.dataSets.reflections.DataSetReflection.*;

public class UsersDAO {

    private final Connection connection;
    private final CacheEngine cache;

    public UsersDAO(Connection connection, CacheEngine cache) {
        this.connection = connection;
        this.cache = cache;
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

        try {
            updateQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTable(Class<?> clazz) {
        final String DELETE_SQL = "DROP TABLE %s";

        String tableName = getTableName(clazz);
        if (tableName.equals("")) throw new RuntimeException("No entity class " + clazz.getSimpleName());

        String query = String.format(DELETE_SQL, tableName);

        try {
            updateQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        long id;
        try {
            id = insertQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        obj.setId(id);

        saveOneToManyMatch(obj);

        return id;

    }

    public <T extends DataSet> T readDataSet(long id, Class<?> clazz) {

        String key = getTableName(clazz) +  "_" + id;
        T dataSetObject;
        MyElement cacheElement;

        cacheElement = cache.get(key);
        if (cacheElement != null) {
            Object obj = cacheElement.getValue();
            if (obj != null) {
                dataSetObject = (T) obj;
            } else {
                dataSetObject = null;
            }
            System.out.println("Read DataSetObject from cache. Key " + key);
        } else {
            String query = "SELECT * FROM " + getTableName(clazz) + " WHERE id = " + id;
            dataSetObject = selectObjectQuery(query, clazz);
            if (dataSetObject != null) {
                cacheElement = new MyElement(key, dataSetObject);
                cache.put(cacheElement);
            }

        }
        return dataSetObject;
    }

    public <T extends DataSet> T readDataSet(String name, Class<T> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE name = " + name + " LIMIT 0,1";
        T dataSetObject = selectObjectQuery(query, clazz);
        if (dataSetObject != null) {
            String key = getTableName(clazz) +  "_" + dataSetObject.getId();
            MyElement cacheElement = new MyElement(key, dataSetObject);
            cache.put(cacheElement);
        }
        return dataSetObject;
    }

    public <T extends DataSet> List<T> readAllDataSet(Class<T> clazz) {
        List<T> dataSetList;
        String query = "SELECT * FROM " + getTableName(clazz);
        Executor exec = new Executor(connection);
        dataSetList = exec.selectAllObjectsQuery(query, clazz);
        for (Iterator<T> iterator = dataSetList.iterator(); iterator.hasNext(); ) {
            T dataSetObject = iterator.next();
            String key = getTableName(clazz) +  "_" + dataSetObject.getId();
            MyElement cacheElement = new MyElement(key, dataSetObject);
            cache.put(cacheElement);
        }
        return dataSetList;
    }

    public <T extends DataSet> List<T> readAllDataSet(String fieldName, String fieldValue, Class<?> clazz) {
        List<T> dataSetList;
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE " + fieldName + " = ?";
        Executor exec = new Executor(connection);
        dataSetList = exec.selectAllObjectsQueryByField(query, fieldValue, clazz);
        for (Iterator<T> iterator = dataSetList.iterator(); iterator.hasNext(); ) {
            T dataSetObject = iterator.next();
            String key = getTableName(clazz) +  "_" + dataSetObject.getId();
            MyElement cacheElement = new MyElement(key, dataSetObject);
            cache.put(cacheElement);
        }
        return dataSetList;
    }

    private int updateQuery(String query) throws SQLException {
        Executor exec = new Executor(connection);
        return exec.updateQuery(query);
    }

    private long insertQuery(String query) throws SQLException {
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
