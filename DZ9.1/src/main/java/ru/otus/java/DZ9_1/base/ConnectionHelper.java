package ru.otus.java.DZ9_1.base;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    static Connection getConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String host = "10.0.0.132";
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
        } catch (Exception se) {
            throw new RuntimeException(se);
        }
    }
}