package myORM.base.dataSets.reflections;

import myORM.base.dataSets.DataSet;
import myORM.base.dataSets.reflections.annotations.*;
import myORM.dbService.DBServiceImpl;

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

    public static String getTableName(Class<?> clazz) {

        if (clazz.isAnnotationPresent(EntityMyORM.class)) {
            if (clazz.isAnnotationPresent(TableMyORM.class)) {
                TableMyORM table = clazz.getAnnotation(TableMyORM.class);
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
                if (field.isAnnotationPresent(IdMyORM.class) && !hasId) {
                    IdMyORM id = field.getAnnotation(IdMyORM.class);
                    columns.add(new TableColumn(field.getName(), id.type(), IS_ID_KEY));
                    hasId = true;
                }
                if (field.isAnnotationPresent(ColumnMyORM.class)) {
                    ColumnMyORM column = field.getAnnotation(ColumnMyORM.class);
                    columns.add(new TableColumn(field.getName(), column.type(), IS_NO_ID_KEY));
                }
                if (field.isAnnotationPresent(OneToOneMyORM.class)) {
                    columns.add(new TableColumn(field.getName() + "_id", "BIGINT(20)", IS_ONE_TO_ONE_MAP));
                } else if (field.isAnnotationPresent(ManyToOneMyORM.class) && field.isAnnotationPresent(JoinColumnMyORM.class)) {
                    JoinColumnMyORM joinColumn = field.getAnnotation(JoinColumnMyORM.class);
                    columns.add(new TableColumn(joinColumn.name(), "BIGINT(20)", IS_MANY_TO_ONE_MAP));
                }
            }
            clazz = clazz.getSuperclass();
        }

        return columns;
    }

    public static Set<ObjectField> getDataSetFields(DataSet obj) {

        Set<ObjectField> fields = new HashSet<>();

        try {
            Class clazz = obj.getClass();

            while (clazz != null) {

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ColumnMyORM.class)) {
                        field.setAccessible(true);
                        fields.add(new ObjectField(field.getName(), field.get(obj)));
                    } else if (field.isAnnotationPresent(OneToOneMyORM.class)) {
                        field.setAccessible(true);
                        if (field.get(obj) != null) {
                            DataSet dataSetObj = (DataSet) field.get(obj);
                            try (DBServiceImpl dbService = new DBServiceImpl()) {
                                dbService.prepareDB(dataSetObj.getClass());
                                dbService.save(dataSetObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            fields.add(new ObjectField(field.getName() + "_id", dataSetObj.getId()));
                        }
                    } else if (field.isAnnotationPresent(ManyToOneMyORM.class) && field.isAnnotationPresent(JoinColumnMyORM.class)) {
                        JoinColumnMyORM joinColumn = field.getAnnotation(JoinColumnMyORM.class);
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
                    if (field.isAnnotationPresent(OneToManyMyORM.class)) {
                        field.setAccessible(true);

                        if (field.getType().equals(java.util.Set.class)) {
                            if (field.get(obj) != null) {
                                Set<DataSet> objectsSet = (Set) field.get(obj);

                                String fieldType = field.getGenericType().getTypeName();
                                String DataSetType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));

                                try (DBServiceImpl dbService = new DBServiceImpl()) {
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
                if (field.isAnnotationPresent(OneToOneMyORM.class)) {
                    try (DBServiceImpl dbService = new DBServiceImpl()) {
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
                } else if (field.isAnnotationPresent(OneToManyMyORM.class)) {
                    OneToManyMyORM column = field.getAnnotation(OneToManyMyORM.class);
                    String joinFiledName = column.mappedBy();

                    field.setAccessible(true);

                    if (field.getType().equals(java.util.Set.class)) {
                        String fieldType = field.getGenericType().getTypeName();
                        String DataSetType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));

                        String objectId = resultSet.getObject("id").toString();
                        String objectFieldName = "";
                        try (DBServiceImpl dbService = new DBServiceImpl()) {
                            Class<?> matchClass = Class.forName(DataSetType);
                            for (Field joinFiled : matchClass.getDeclaredFields()) {
                                if (joinFiled.getName().equals(joinFiledName)) {
                                    if (joinFiled.isAnnotationPresent(JoinColumnMyORM.class)) {
                                        JoinColumnMyORM joinColumn = joinFiled.getAnnotation(JoinColumnMyORM.class);
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
                } else if (field.isAnnotationPresent(ColumnMyORM.class) || field.isAnnotationPresent(IdMyORM.class)) {
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
