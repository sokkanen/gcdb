package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserPlatformDao implements Dao<Platform, Platform> {

    private User user;
    private Database db;

    public UserPlatformDao(User user, Database db) {
        this.user = user;
        this.db = db;
    }

    @Override
    public List<Platform> findAll(Platform arg) throws SQLException {
        List<Platform> lst = new ArrayList<>();
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT Platform.name AS name, Platform.id AS id FROM Platform, UserPlatform "
                    + "WHERE UserPlatform.user_id = ? AND UserPlatform.platform_id = Platform.id");
            stmt.setInt(1, user.getId());
            ResultSet rsset = stmt.executeQuery();
            while (rsset.next()) {
                lst.add(new Platform(rsset.getString("name"), rsset.getInt("id")));
            }
            rsset.close();
            connection.close();
        }
        return lst;
    }

    @Override
    public boolean save(Platform pltfrm) throws SQLException {
        if (findIfAlreadyListed(pltfrm)) {
            return false;
        }
        try (Connection conn = db.newConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserPlatform(user_id, platform_id) VALUES (?,?)");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, pltfrm.getId());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    @Override
    public boolean delete(Platform toBeRemoved) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM UserPlatform "
                    + "WHERE user_id = ? AND platform_id = ?");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, toBeRemoved.getId());
            stmt.executeUpdate();
            connection.close();
            return true;
        }
    }

    public boolean findIfAlreadyListed(Platform pltfrm) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT user_id, platform_id FROM UserPlatform WHERE user_id = ? AND platform_id = ?");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, pltfrm.getId());
            ResultSet rsset = stmt.executeQuery();
            int userid = rsset.getInt("user_id");
            int platformid = rsset.getInt("platform_id");
            rsset.close();
            stmt.close();
            connection.close();
            if (userid != 0 && platformid != 0) {
                return true;
            }
            return false;
        } catch (SQLException error) {
        }
        return false;
    }

    @Override
    public Platform findOne(Platform arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
