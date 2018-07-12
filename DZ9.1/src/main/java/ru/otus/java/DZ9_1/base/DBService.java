package ru.otus.java.DZ9_1.base;

import ru.otus.java.DZ9_1.data.DataSet;
import ru.otus.java.DZ9_1.executor.QueryExecutor;

import java.sql.Connection;
import java.sql.SQLException;

public class DBService implements AutoCloseable {
    private final Connection connection;

    public DBService() {
        connection = ConnectionHelper.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public String getMetaData() {
        try {
            return "Connected to " + connection.getMetaData().getURL() + "\n" +
                    "DB product name " + connection.getMetaData().getDatabaseProductName() + "\n" +
                    "DB user " + connection.getMetaData().getUserName() + "\n" +
                    "DB version " + connection.getMetaData().getDatabaseMajorVersion() + "\n" +
                    "DB driver " + connection.getMetaData().getDriverName() + "\n";
        } catch (Exception se) {
            se.printStackTrace();
            return se.getMessage();
        }
    }

    public void prepareDB(String query) throws SQLException {
        updateQuery(query);
    }

    public int updateQuery(String query) throws SQLException {
        QueryExecutor exec = new QueryExecutor(connection);
        return exec.updateQuery(query);
    }

    public long insertQuery(String query) throws SQLException {
        QueryExecutor exec = new QueryExecutor(connection);
        return exec.insertQuery(query);
    }

    public void selectQuery(String query, QueryExecutor.ResultHandler handler) throws SQLException {
        QueryExecutor exec = new QueryExecutor(connection);
        exec.selectQuery(query, handler);
    }

    public <T extends DataSet> T selectObjectQuery(String query, Class<T> dataSet) throws SQLException {
        QueryExecutor exec = new QueryExecutor(connection);
        return exec.selectObjectQuery(query, dataSet);
    }

    @Override
    public void close() throws Exception {
        if (connection != null || !connection.isClosed()) {
            connection.close();
            //System.out.println("Connection closed");
        }
    }
}