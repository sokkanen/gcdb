/**
 * Dao-luokka pelialustojen tietokantahallintaa varten.
 */
package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlatformDao implements Dao {

    private Database db;
    private User user;
    private UserPlatformDao userPlatform;

    public PlatformDao(Database db, User user) {
        this.db = db;
        this.user = user;
        this.userPlatform = new UserPlatformDao(user, db);
    }

    @Override
    public List findAll(Object arg) throws SQLException {
        return userPlatform.findAll(arg);
    }

    @Override
    public Object findOne(Object arg) throws SQLException {
        Platform pltfrm = (Platform) arg;
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT name, id FROM Platform WHERE name = ?");
            stmt.setString(1, pltfrm.getName());
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                return null;
            }
            return new Platform(rsset.getString("name"), rsset.getInt("id"));
        }
    }

    @Override
    public boolean save(Object arg) throws SQLException {
        if (findOne(arg) != null) {
            return false;
        }
        Platform pltfrm = (Platform) arg;
        String toBeInserted = pltfrm.getName();
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Platform(name) VALUES (?)");
            stmt.setString(1, toBeInserted);
            stmt.executeUpdate();
            pltfrm = (Platform) findOne(arg);
            userPlatform.save(pltfrm);
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    @Override
    public void delete(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
