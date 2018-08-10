package myORM.dbService;

import myORM.base.ConnectionHelper;
import myORM.base.DBService;
import myORM.base.dataSets.DataSet;
import myORM.dbService.dao.UsersDAO;
import myORM.base.dataSets.UserDataSet;

import java.sql.Connection;
import java.util.List;

public class DBServiceImpl implements DBService, AutoCloseable {
    private final UsersDAO dao;
    Connection connection = ConnectionHelper.getConnection();

    public DBServiceImpl() {
        dao = new UsersDAO(connection);
    }

    public void prepareDB(Class<?> clazz) {
        dao.prepareDB(clazz);
    }

    public void deleteTable(Class<?> clazz) {
        dao.deleteTable(clazz);
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
