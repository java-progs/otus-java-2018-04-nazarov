package myORM.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String host = "192.168.56.101";
            String port = "3306";
            String user = "root";
            String password = "root";
            String dbName = "otusexample";

            String url = "jdbc:mysql://" +
                    host + ":" +
                    port + "/" +
                    dbName + "?user=" +
                    user + "&password=" +
                    password + "&useSSL=false";

            return DriverManager.getConnection(url);
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }
    }
}
