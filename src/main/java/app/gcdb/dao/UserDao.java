package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dao-luokka käyttäjätietojen tietokantahallintaa varten. Käyttää tietokannan
 * User-taulua.
 *
 */
public class UserDao implements Dao<User, String> {

    private Database db;

    public UserDao(Database db) {
        this.db = db;
        try {
            createTable();
        } catch (SQLException ex) {
        }
    }

    /**
     * Luo tietokantataulun User, mikäli taulua ei löydy ennestään.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public void createTable() throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement(""
                    + "CREATE TABLE IF NOT EXISTS User (id integer PRIMARY KEY, username varchar(20), passhash integer);");
            stmt.execute();
        }
    }

    /**
     * Hakee yhden käyttäjän nimen (String) perusteella.
     *
     * @param searchString Haettava käyttäjätunnus.
     *
     * @return Palauttaa haetun käyttäjän ja salasanatiivisteen tai null, mikäli
     * käyttäjää ei löydy.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public User findOne(String searchString) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT username, passhash, id FROM User WHERE username = ?");
            stmt.setString(1, searchString);
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                return null;
            }

            return new User(rsset.getString("username"), rsset.getInt("passhash"), rsset.getInt("id"));
        }
    }

    /**
     * Tallentaa uuden käyttäjän tietokantaan.
     *
     * @param user Tallennettava käyttäjä.
     *
     * @return false, mikäli parametrina annetun käyttäjän käyttäjätunnus on
     * tyhjä. Palauttaa true, kun käyttäjä on tallennettu tietokantaan.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    public boolean save(User user) throws SQLException {
        if (findOne(user.getUsername()) != null) {
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
    public List<User> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(String arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean save(String arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
