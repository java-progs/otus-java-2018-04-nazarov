package ru.otus.java.DZ9_1.executor;

import ru.otus.java.DZ9_1.data.DataSet;

import java.sql.*;

import static ru.otus.java.DZ9_1.data.reflections.DataSetReflection.createDataSet;

public class QueryExecutor {
    private Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public long insertQuery(String query) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            Long id = -1L;
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
            return id;
        }
    }

    public int updateQuery(String query) throws SQLException {
        try (Statement stm = connection.createStatement()) {
            stm.execute(query);
            return stm.getUpdateCount();
        }
    }

    public void selectQuery(String query, ResultHandler handler) throws SQLException {
        try (Statement stm = connection.createStatement()) {
            stm.execute(query);
            ResultSet result = stm.getResultSet();
            handler.handle(result);
        }
    }

    public <T extends DataSet> T selectObjectQuery(String query, Class<T> clazz) throws SQLException {
        try (Statement stm = connection.createStatement()) {
            stm.execute(query);
            ResultSet result = stm.getResultSet();
            T selectDataSet = createDataSet(result, clazz);

            return selectDataSet;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @FunctionalInterface
    public interface ResultHandler {
        void handle(ResultSet result) throws SQLException;
    }
}