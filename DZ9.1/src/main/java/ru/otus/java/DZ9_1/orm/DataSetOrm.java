package ru.otus.java.DZ9_1.orm;

import ru.otus.java.DZ9_1.base.DBService;
import ru.otus.java.DZ9_1.data.DataSet;
import ru.otus.java.DZ9_1.data.reflections.TableColumn;

import java.util.Map;
import java.util.Set;

import static ru.otus.java.DZ9_1.data.reflections.DataSetReflection.*;

public class DataSetOrm {

    public static void prepareDB(Class<?> clazz) {
        final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS %s (%s)";

        String tableName = getTableName(clazz);
        if (tableName.equals("")) throw new RuntimeException("No entity class " + clazz.getSimpleName());
        System.out.println("Table name: " + tableName);

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

        String createTableSQL = String.format(CREATE_SQL, tableName, columns);

        try (DBService dbService = new DBService()) {
            dbService.prepareDB(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearDB(Class<?> clazz) {
        final String DELETE_SQL = "DROP TABLE %s";

        String tableName = getTableName(clazz);
        if (tableName.equals("")) throw new RuntimeException("No entity class " + clazz.getSimpleName());

        String deleteTableSQL = String.format(DELETE_SQL, tableName);

        try (DBService dbService = new DBService()) {
            dbService.prepareDB(deleteTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearDB() {

    }

    public static void saveDataSet(DataSet obj) {
        Map<String, Object> fields = getDataSetFields(obj);

        StringBuilder fieldsNames = new StringBuilder();
        StringBuilder fieldsValues = new StringBuilder();

        for (Map.Entry<String, Object> pair : fields.entrySet()) {
            fieldsNames.append(pair.getKey() + ",");
            fieldsValues.append(prepareValue(pair.getValue()));
        }

        if (fieldsNames.length() > 0) {
            fieldsNames.deleteCharAt(fieldsNames.length() - 1);
        }

        if (fieldsValues.length() > 0) {
            fieldsValues.deleteCharAt(fieldsValues.length() - 1);
        }

        String query = "INSERT INTO " + getTableName(obj.getClass()) +" (" + fieldsNames + ") VALUES (" + fieldsValues + ")";

        try (DBService dbService = new DBService()) {
            long id = dbService.insertQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prepareValue(Object value) {
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

    public static <T extends DataSet> T getDataSet(long id, Class<T> clazz) {
        String query = "SELECT * FROM " + getTableName(clazz) + " WHERE id = " + id;
        try (DBService dbService = new DBService()) {
            return dbService.selectObjectQuery(query, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}