package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;  //provide additionl info about failure in database

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/Demo_EAD";  //database name IMS
    private static final String USER = "root";      //mysql username
    private static final String PASSWORD = "root";  //mysql password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}