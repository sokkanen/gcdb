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

public class PlatformDao implements Dao<Platform, Platform> {

    private Database db;
    private User user;
    private UserPlatformDao userPlatform;

    public PlatformDao(Database db, User user) {
        this.db = db;
        this.user = user;
        this.userPlatform = new UserPlatformDao(user, db);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public List findAll(Platform arg) throws SQLException {
        return userPlatform.findAll(arg);
    }

    @Override
    public Platform findOne(Platform pltfrm) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT name, id FROM Platform WHERE name = ?");
            stmt.setString(1, pltfrm.getName().trim().toUpperCase());
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                return null;
            }
            return new Platform(rsset.getString("name"), rsset.getInt("id"));
        }
    }

    @Override
    public boolean save(Platform arg) throws SQLException {
        Platform pltfrm = (Platform) arg;
        if (findOne(arg) != null) {
            pltfrm = (Platform) findOne(arg);
            if (userPlatform.save(pltfrm)) {
                return true;
            }
            return false;
        }
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Platform(name) VALUES (?)");
            stmt.setString(1, pltfrm.getName().trim().toUpperCase());
            stmt.executeUpdate();
            pltfrm = (Platform) findOne(arg);
            userPlatform.save(pltfrm);
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    @Override
    public boolean delete(Platform arg) throws SQLException {
        return userPlatform.delete(arg);
    }

}
