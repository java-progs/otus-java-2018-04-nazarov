package myORM.base.dataSets.reflections;

import myORM.base.dataSets.DataSet;
import myORM.dbService.MyDBServiceImpl;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSetReflection {

    public static final String IS_ID_KEY = "1";
    public static String IS_NO_ID_KEY = "2";
    public static String IS_ONE_TO_ONE_MAP = "3";
    public static String IS_MANY_TO_ONE_MAP = "4";


    public static String DB_TYPE_LONG = "BIGINT(20)";
    public static String DB_TYPE_INT = "INT";
    public static String DB_TYPE_DOUBLE = "DOUBLE";
    public static String DB_TYPE_STRING = "VARCHAR(255)";

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
                    columns.add(new TableColumn(field.getName(), DB_TYPE_LONG, IS_ID_KEY));
                    hasId = true;
                }
                if (field.isAnnotationPresent(Column.class)) {
                    String type = getColumnType(field);
                    columns.add(new TableColumn(field.getName(), type, IS_NO_ID_KEY));
                }
                if (field.isAnnotationPresent(OneToOne.class)) {
                    columns.add(new TableColumn(field.getName() + "_id", DB_TYPE_LONG, IS_ONE_TO_ONE_MAP));
                } else if (field.isAnnotationPresent(ManyToOne.class) && field.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    columns.add(new TableColumn(joinColumn.name(), DB_TYPE_LONG, IS_MANY_TO_ONE_MAP));
                }
            }
            clazz = clazz.getSuperclass();
        }

        return columns;
    }

    private static String getColumnType(Field field) {
        Class<?> claz = field.getType();
        String typeName = claz.getName();
        switch (typeName) {
            case "java.lang.String" :
                return DB_TYPE_STRING;
            case "java.lang.Integer" :
                return DB_TYPE_INT;
            case "java.lang.Double" :
                return DB_TYPE_DOUBLE;
            case "int" :
                return DB_TYPE_INT;
            case "double" :
                return DB_TYPE_DOUBLE;
            default:
                return DB_TYPE_STRING;
        }
    }

    public static Set<ObjectField> getDataSetFields(DataSet obj) {

        Set<ObjectField> fields = new HashSet<>();

        try {
            Class clazz = obj.getClass();

            while (clazz != null) {

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        fields.add(new ObjectField(field.getName(), field.get(obj)));
                    } else if (field.isAnnotationPresent(OneToOne.class)) {
                        field.setAccessible(true);
                        if (field.get(obj) != null) {
                            DataSet dataSetObj = (DataSet) field.get(obj);
                            try (MyDBServiceImpl dbService = new MyDBServiceImpl()) {
                                dbService.prepareDB(dataSetObj.getClass());
                                dbService.save(dataSetObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            fields.add(new ObjectField(field.getName() + "_id", dataSetObj.getId()));
                        }
                    } else if (field.isAnnotationPresent(ManyToOne.class) && field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        field.setAccessible(true);
                        DataSet dataSetObject = (DataSet) field.get(obj);
                        fields.add(new ObjectField(joinColumn.name(), dataSetObject.getId()));
                    }
                }
                clazz = clazz.getSuperclass();
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fields;
    }

    public static void saveOneToManyMatch(DataSet obj) {
        try {
            Class clazz = obj.getClass();

            while (clazz != null) {

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(OneToMany.class)) {
                        field.setAccessible(true);

                        if (field.getType().equals(java.util.Set.class)) {
                            if (field.get(obj) != null) {
                                Set<DataSet> objectsSet = (Set) field.get(obj);

                                String fieldType = field.getGenericType().getTypeName();
                                String DataSetType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));

                                try (MyDBServiceImpl dbService = new MyDBServiceImpl()) {
                                    Class<?> dataSetClass = Class.forName(DataSetType);
                                    dbService.prepareDB(dataSetClass);
                                    for (DataSet o : objectsSet) {
                                        dbService.save(o);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T extends DataSet> T createDataSet(ResultSet resultSet, Class<?> clazz) throws SQLException, InstantiationException, IllegalAccessException {

        Object dataSet = clazz.newInstance();
        Class dataSetClass = dataSet.getClass();

        while (dataSetClass != null) {

            for (Field field : dataSetClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(OneToOne.class)) {
                    try (MyDBServiceImpl dbService = new MyDBServiceImpl()) {
                        if (resultSet.getObject(field.getName() + "_id") != null) {
                            boolean fieldAccess = field.isAccessible();
                            field.setAccessible(true);
                            long idObjectDB = (Long) resultSet.getObject(field.getName() + "_id");
                            Class<?> dataSetType = field.getType();
                            Object dataSetObject = dbService.readDataSet(idObjectDB, dataSetType);

                            field.set(dataSet, dataSetObject);
                            field.setAccessible(fieldAccess);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    OneToMany column = field.getAnnotation(OneToMany.class);
                    String joinFiledName = column.mappedBy();

                    field.setAccessible(true);

                    if (field.getType().equals(java.util.Set.class)) {
                        String fieldType = field.getGenericType().getTypeName();
                        String DataSetType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));

                        String objectId = resultSet.getObject("id").toString();
                        String objectFieldName = "";
                        try (MyDBServiceImpl dbService = new MyDBServiceImpl()) {
                            Class<?> matchClass = Class.forName(DataSetType);
                            for (Field joinFiled : matchClass.getDeclaredFields()) {
                                if (joinFiled.getName().equals(joinFiledName)) {
                                    if (joinFiled.isAnnotationPresent(JoinColumn.class)) {
                                        JoinColumn joinColumn = joinFiled.getAnnotation(JoinColumn.class);
                                        objectFieldName = joinColumn.name();
                                    }
                                    break;
                                }
                            }

                            List<DataSet> objects = dbService.readAllByField(objectFieldName, objectId, matchClass);
                            Set<DataSet> setObjects = new HashSet<>(objects);

                            field.set(dataSet, setObjects);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Id.class)) {
                    boolean fieldAccess = field.isAccessible();
                    field.setAccessible(true);
                    field.set(dataSet, resultSet.getObject(field.getName(), field.getType()));
                    field.setAccessible(fieldAccess);
                }
            }

            dataSetClass = dataSetClass.getSuperclass();
        }

        return ((T) dataSet);
    }
}
