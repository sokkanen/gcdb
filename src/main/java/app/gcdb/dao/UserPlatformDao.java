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

public class UserPlatformDao implements Dao<String, Object> {

    private User user;
    private Database db;

    public UserPlatformDao(User user, Database db) {
        this.user = user;
        this.db = db;
    }

    @Override
    public List<String> findAll(Object arg) throws SQLException {
        List<String> lst = new ArrayList<>();
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT Platform.name AS name FROM Platform, UserPlatform "
                    + "WHERE UserPlatform.user_id = ? AND UserPlatform.platform_id = Platform.id");
            stmt.setInt(1, user.getId());
            ResultSet rsset = stmt.executeQuery();
            while (rsset.next()) {
                lst.add(rsset.getString("name"));
            }
        }
        return lst;
    }

    @Override
    public boolean save(Object arg) throws SQLException {
        Platform pltfrm = (Platform) arg;
        if (!findOne(arg).isEmpty()){
            return false;
        }
        try (Connection conn = db.newConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserPlatform(user_id, platform_id) VALUES (?,?)");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, pltfrm.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    @Override
    public void delete(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String findOne(Object arg) throws SQLException {
        Platform pltfrm = (Platform) arg;
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT user_id, platform_id FROM UserPlatform WHERE user_id = ? AND platform_id = ?");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, pltfrm.getId());
            ResultSet rsset = stmt.executeQuery();
            String userAndPlatform = rsset.getInt("user_id") + rsset.getInt("platform_id") + "";
            rsset.close();
            connection.close();
            return userAndPlatform;
        } catch (SQLException error){
            System.out.println("findone: " + error.getMessage());
        }
        return "";
    }
}
