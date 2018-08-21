package myORM.dbService;

import myORM.base.ConnectionHelper;
import myORM.base.DBService;
import myORM.base.dataSets.DataSet;
import myORM.cache.CacheEngine;
import myORM.cache.MyCacheEngineImpl;
import myORM.dbService.dao.UsersDAO;
import myORM.base.dataSets.UserDataSet;

import java.sql.Connection;
import java.util.List;

public class MyDBServiceImpl implements DBService, AutoCloseable {
    private final UsersDAO dao;
    Connection connection = ConnectionHelper.getConnection();
    private static final CacheEngine cache = new MyCacheEngineImpl(100, 1000, 0, false);

    public MyDBServiceImpl() {
        dao = new UsersDAO(connection, cache);
    }

    public void prepareDB(Class<?> clazz) {
        dao.prepareDB(clazz);
    }

    public void deleteTable(Class<?> clazz) {
        dao.deleteTable(clazz);
    }

    public CacheEngine getCache() {
        return cache;
    }

    @Override
    public String getLocalStatus() {
        return null;
    }

    @Override
    public void save(UserDataSet dataSet) {
        long id = dao.insertDataSetObject(dataSet);
        dataSet.setId(id);
    }

    @Override
    public UserDataSet read(long id) {
        return dao.readDataSet(id, UserDataSet.class);
    }

    @Override
    public UserDataSet readByName(String name) {
        return dao.readDataSet(name, UserDataSet.class);
    }

    @Override
    public List<UserDataSet> readAll() {
        return dao.readAllDataSet(UserDataSet.class);
    }

    public List<DataSet> readAllByField(String fieldName, String fieldValue, Class<?> clazz) {
        return dao.readAllDataSet(fieldName, fieldValue, clazz);
    }

    public void save(DataSet dataSet) {
        //long id = dao.insertDataSetObject(dataSet);
        dao.insertDataSetObject(dataSet);
    }

    public <T extends DataSet> T readDataSet(long id, Class<?> clazz) {
        return dao.readDataSet(id, clazz);
    }

    @Override
    public void shutdown() {
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null || !connection.isClosed()) {
            connection.close();
            //System.out.println("Connection closed");
        }
    }
}
