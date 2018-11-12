package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements Dao {

    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }

    @Override
    public Object findOne(Object arg) throws SQLException {
        User user = (User) arg;
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT username, passhash FROM User WHERE username = ?");
            stmt.setString(1, user.getUsername());
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                return null;
            }

            return new User(rsset.getString("username"), rsset.getInt("passhash"));
        }
    }

    @Override
    public boolean save(Object arg) throws SQLException {
        User user = (User) arg;
        if (findOne(user) != null) {
            return false;
        }

        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO User (username, passhash) VALUES (?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setInt(2, user.getPassHash());
            stmt.executeUpdate();
            return true;
        }
    }

    @Override
    public void delete(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not implemented on this class.");
    }
}
