package repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    public static Connection getConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/crypto";
        String user = "root";
        String password = "1234";

        return DriverManager.getConnection(url, user, password);
    }
}
