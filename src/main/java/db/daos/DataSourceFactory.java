package db.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceFactory {

    private static final String DB_URL = "jdbc:sqlite:sqlite.db";
    private static final String DB_USER = ""; 
    private static final String DB_PASSWORD = ""; 

    private DataSourceFactory() {
        throw new IllegalStateException("This is a static class that should not be instantiated");
    }

    /**
     * @return a new connection to the database using DriverManager
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
