package app.gcdb.dao;

import app.gcdb.database.Database;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dao-luokka pelialustojen tietokantahallintaa varten. Käyttää UserPlatformDao
 * -luokkaa käyttäjään liittyvien pelialustojen tietokantahallintaan. Käyttää
 * tietokannan Platform-taulua.
 *
 */
public class PlatformDao implements Dao<Platform, Platform> {

    private Database db;
    private User user;
    private UserPlatformDao userPlatform;

    public PlatformDao(Database db, User user) {
        this.db = db;
        this.user = user;
        try {
            createTable();
        } catch (SQLException ex) {
        }
        this.userPlatform = new UserPlatformDao(user, db);
    }

    /**
     * Luo tietokantataulun Platform, mikäli taulua ei löydy ennestään.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public void createTable() throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement(""
                    + "CREATE TABLE IF NOT EXISTS Platform (id integer PRIMARY KEY, name varchar(50));");
            stmt.execute();
        }
    }

    /**
     * Luokan konstruktorin käyttämä metodi kirjautuneen käyttäjän
     * asettamiseksi.
     * 
     * @param user Asetettava käyttäjä
     *
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Hakee kaikki käyttäjän pelialustat.
     *
     * @see UserPlatformDao#findAll()
     *
     * @return Palauttaa UserPlatformDao:n palauttaman alustalistan.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public List findAll() throws SQLException {
        return userPlatform.findAll();
    }

    /**
     * Hakee yhden pelialustan nimen perusteella.
     *
     * @param pltfrm Haettava pelialusta.
     *
     * @return Palauttaa haetun alustan tai null, mikäli alustaa ei löydy.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
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

    /**
     * Tallentaa uuden pelialustan tietokantaan. Kutsuu lisäksi
     * userPlatform-oliota tallentamaan ko. pelialusta käyttäjälle.
     *
     * @param arg Tallennettava pelialusta.
     *
     * @see UserPlatformDao#save(app.gcdb.domain.Platform)
     *
     * @return Palauttaa UserPlatform-olion palauttaman arvon, mikäli alusta on
     * jo tietokannassa, mutta ei lisättynä kirjautuneella käyttäjällä.
     * Palauttaa true, mikäli alusta tallennetaan sekä alustatauluun, että
     * kirjautuneelle käyttäjälle. Palauttaa false, mikäli tapahtuu
     * SQLException.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public boolean save(Platform arg) throws SQLException {
        Platform pltfrm = (Platform) arg;
        if (findOne(arg) != null) {
            pltfrm = findOne(arg);
            return userPlatform.save(pltfrm);
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

    /**
     * Poistaa alustan. Poistaa alustan ensin käyttäjältä. Jos yhdelläkään
     * käyttäjälle ei ole enää alustaa listattuna, poistaa alustan
     * tietokannasta.
     *
     * @param pltfrm Poistettava alusta
     *
     * @return palauttaa true, jos alusta on poistettu koko tietokannasta.
     * Palauttaa false, jos poistettu vain käyttäjältä tai tapahtuu
     * SQLException.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     *
     * @see UserPlatformDao#noUserHasPlatform(app.gcdb.domain.Platform)
     * @see UserPlatformDao#delete(app.gcdb.domain.Platform)
     */
    @Override
    public boolean delete(Platform pltfrm) throws SQLException {
        userPlatform.delete(pltfrm);
        if (userPlatform.noUserHasPlatform(pltfrm)) {
            try (Connection connection = db.newConnection()) {
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM Platform WHERE platform_id = ?");
                stmt.setInt(1, pltfrm.getId());
                stmt.executeUpdate();
                stmt.close();
                connection.close();
                return true;
            } catch (SQLException error) {
                return false;
            }
        }
        return false;
    }
}
