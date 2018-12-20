package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.Game;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dao-luokka pelien tietokantahallintaa varten. Käyttää tietokannan
 * Game-taulua.
 *
 */
public class GameDao implements Dao<Game, Game> {

    private User user;
    private Database db;

    public GameDao(Database database, User user) {
        this.user = user;
        this.db = database;
        try {
            createTable();
        } catch (SQLException ex) {
        }
    }

    /**
     * Luo tietokantataulun Game, mikäli taulua ei löydy ennestään.
     */
    @Override
    public void createTable() throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement(""
                    + "CREATE TABLE IF NOT EXISTS Game "
                    + "(id integer PRIMARY KEY, user_id integer, platform_id integer, name varchar(100), "
                    + "condition integer, content integer, comment varchar(500), region varchar(50), "
                    + "FOREIGN KEY(user_id) REFERENCES User(id), FOREIGN KEY(platform_id) REFERENCES Platform(id));");
            stmt.execute();
        }
    }

    /**
     * Luokan konstruktorin käyttämä metodi kirjautuneen käyttäjän
     * asettamiseksi.
     *
     * @param user Asetettava käyttäjä
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Hakee kaikki käyttäjän pelit.
     *
     * @return Palauttaa pelilistan.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public List<Game> findAll() throws SQLException {
        List<Game> lst = new ArrayList<>();
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Game WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            ResultSet rsset = stmt.executeQuery();
            while (rsset.next()) {
                lst.add(new Game(rsset.getString("name"), rsset.getInt("platform_id"), rsset.getInt("condition"), rsset.getInt("content"), rsset.getInt("id"), rsset.getString("region"), rsset.getString("comment")));
            }
            rsset.close();
            connection.close();
        }
        return lst;
    }

    /**
     * Hakee yhden pelin pelin sisäisten muuttujien perusteella.
     *
     * @param game Haettava peli.
     *
     * @return Palauttaa null, jos peliä ei löydy. Palauttaa pelin, jos se
     * löytyy tietokannasta.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public Game findOne(Game game) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Game WHERE user_id = ? AND name = ? AND comment = ? AND region = ? AND condition = ? AND content = ?");
            stmt.setInt(1, user.getId());
            stmt.setString(2, game.getName());
            stmt.setString(3, game.getComment());
            stmt.setString(4, game.getRegion());
            stmt.setInt(5, game.getCondition());
            stmt.setInt(6, game.getContent());
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                return null;
            }
            return new Game(rsset.getString("name"), rsset.getInt("platform_id"), rsset.getInt("condition"), rsset.getInt("content"), rsset.getInt("id"), rsset.getString("region"), rsset.getString("comment"));
        }
    }

    /**
     * Tallentaa uuden pelin tietokantaan.
     *
     * @param game Tallennettava peli.
     *
     * @return Palauttaa false, jos peli on jo tietokannassa. Palauttaa true,
     * jos tallentaminen onnistui.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public boolean save(Game game) throws SQLException {
        if (findOne(game) != null) {
            return false;
        }
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Game(user_id, platform_id, name, condition, content, region, comment) VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, game.getPlatform());
            stmt.setString(3, game.getName());
            stmt.setInt(4, game.getCondition());
            stmt.setInt(5, game.getContent());
            stmt.setString(6, game.getRegion());
            stmt.setString(7, game.getComment());
            stmt.executeUpdate();
            stmt.close();
            connection.close();
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    /**
     * Poistaa pelin tietokannasta.
     *
     * @param game Poistettava peli.
     *
     * @return Palauttaa true, jos peliä on yritetty poistaa. Palauttaa false,
     * jos tapahtuu SQLException.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public boolean delete(Game game) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Game "
                    + "WHERE id = ?");
            stmt.setInt(1, game.getId());
            stmt.executeUpdate();
            stmt.close();
            connection.close();
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

}
