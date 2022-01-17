package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    public static Connection getConnection() {
        Connection conx;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/banque?characterEncoding=UTF-8";
            String username = "root";
            String password = "";

            conx = (Connection) DriverManager.getConnection(url, username, password);
            return conx;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
