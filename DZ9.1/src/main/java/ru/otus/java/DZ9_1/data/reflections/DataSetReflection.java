package ru.otus.java.DZ9_1.data.reflections;

import ru.otus.java.DZ9_1.data.DataSet;
import ru.otus.java.DZ9_1.data.reflections.annotations.Column;
import ru.otus.java.DZ9_1.data.reflections.annotations.Entity;
import ru.otus.java.DZ9_1.data.reflections.annotations.Id;
import ru.otus.java.DZ9_1.data.reflections.annotations.Table;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataSetReflection {

    private static Map<String, Object> fields;
    public static final String IS_ID_KEY = "1";
    public static String IS_NO_ID_KEY = "2";

    public static String getTableName(Class<?> clazz) {

        if (clazz.isAnnotationPresent(Entity.class)) {
            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = clazz.getAnnotation(Table.class);
                return table.name();
            } else {
                return clazz.getSimpleName();
            }
        }

        return "";
    }

    public static Set<TableColumn> getDataSetColumns(Class<?> clazz) {

        Set<TableColumn> columns = new HashSet<>();
        boolean hasId = false;

        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Id.class) && !hasId) {
                    Id id = field.getAnnotation(Id.class);
                    columns.add(new TableColumn(field.getName(), id.type(), IS_ID_KEY));
                    hasId = true;
                }
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    columns.add(new TableColumn(field.getName(), column.type(), IS_NO_ID_KEY));
                }
            }
            clazz = clazz.getSuperclass();
        }

        return columns;
    }

    public static Map<String, Object> getDataSetFields(DataSet obj) {

        try {
            Class clazz = obj.getClass();

            fields = new HashMap<>();

            while (clazz != null) {

                for (Field f : clazz.getDeclaredFields()) {
                    if (f.isAnnotationPresent(Column.class)) {
                        f.setAccessible(true);
                        fields.put(f.getName(), f.get(obj));
                    }
                }
                clazz = clazz.getSuperclass();
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fields;
    }

    public static <T extends DataSet> T createDataSet(ResultSet resultSet, Class<T> clazz) throws SQLException, InstantiationException, IllegalAccessException {

        T dataSet = clazz.newInstance();
        if (resultSet.next()) {
            for (Field field : dataSet.getClass().getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    field.set(dataSet, resultSet.getObject(field.getName(), field.getType()));
                    field.setAccessible(false);
                } else {
                    field.set(dataSet, resultSet.getObject(field.getName(), field.getType()));
                }
            }
            dataSet.setId(resultSet.getLong("id"));
        }

        return dataSet;
    }
}