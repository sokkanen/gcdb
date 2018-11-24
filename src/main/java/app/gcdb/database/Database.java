package app.gcdb.database;

import java.sql.*;

public class Database {

    private String sqlAddress;
    private Connection connection;

    public Database(String address) {
        this.sqlAddress = address;
    }

    public Connection newConnection() throws SQLException {
        this.connection = DriverManager.getConnection(sqlAddress);
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        } 
    }
}
