package myORM.dbService.executor;

import myORM.base.dataSets.DataSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static myORM.base.dataSets.reflections.DataSetReflection.createDataSet;

public class Executor {
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public long insertQuery(String query) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            Long id = -1L;
            stm.execute();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
            connection.commit();
            return id;
        } catch (SQLException se) {
            se.printStackTrace();
            connection.rollback();
            return -1;
        }
        finally {
            connection.setAutoCommit(true);
        }
    }

    public int updateQuery(String query) throws SQLException {

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stm.execute();
            return stm.getUpdateCount();
        } catch (SQLException se) {
            se.printStackTrace();
            connection.rollback();
            return -1;
        }
        finally {
            connection.setAutoCommit(true);
        }
    }

    public void selectQuery(String query, ResultHandler handler) {

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.execute();
            ResultSet result = stm.getResultSet();
            handler.handle(result);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public <T extends DataSet> T selectObjectQuery(String query, Class<?> clazz) {

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.execute();
            ResultSet result = stm.getResultSet();

            if (result.next()) {
                return createDataSet(result, clazz);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public <T extends DataSet> List<T> selectAllObjectsQuery(String query, Class<T> clazz) {
        List<T> dataSetList = new ArrayList<>();

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.execute();
            ResultSet result = stm.getResultSet();

            while(result.next()) {
                dataSetList.add(createDataSet(result, clazz));
            }

            return dataSetList;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public <T extends DataSet> List<T> selectAllObjectsQueryByField(String query, String condidtionValue, Class<?> clazz) {
        List<T> dataSetList = new ArrayList<>();

        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, condidtionValue);
            stm.execute();
            ResultSet result = stm.getResultSet();

            while(result.next()) {
                dataSetList.add(createDataSet(result, clazz));
            }

            return dataSetList;
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
