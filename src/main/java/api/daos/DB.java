package api.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    public static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/crypto";
        String user = "nimauser";
        String password = "1234";

        return DriverManager.getConnection(url, user, password);
    }
}
