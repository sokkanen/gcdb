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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dao-luokka käyttäjään liittyen pelialustojen tietokantahallintaa varten.
 * Käyttää tietokannan UserPlatform-taulua.
 *
 */
public class UserPlatformDao implements Dao<Platform, Platform> {

    private User user;
    private Database db;

    public UserPlatformDao(User user, Database db) {
        this.user = user;
        this.db = db;
        try {
            createTable();
        } catch (SQLException ex) {
        }
    }

    /**
     * Luo tietokantataulun UserPlatform, mikäli taulua ei löydy ennestään.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public void createTable() throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement(""
                    + "CREATE TABLE IF NOT EXISTS UserPlatform (user_id integer, platform_id integer, "
                    + "FOREIGN KEY(user_id) REFERENCES User(id), FOREIGN KEY(platform_id) REFERENCES Platform(id));");
            stmt.execute();
        }
    }

    /**
     * Hakee kaikki käyttäjän pelialustat.
     *
     * @see PlatformDao#findAll()
     *
     * @return Palauttaa listan käyttäjän pelialustoista.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public List<Platform> findAll() throws SQLException {
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

    /**
     * Tallentaa uuden käyttäjään liittyvän pelialustan tietokantaan.
     *
     * @param pltfrm Tallennettava pelialusta.
     *
     * @see PlatformDao#save(app.gcdb.domain.Platform)
     *
     * @return Palauttaa false, mikäli tapahtuu SQLException tai mikäli
     * pelialusta on jo lisätty käyttäjälle. Palauttaa true, kun alusta on
     * tallennettu käyttäjälle.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     */
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

    /**
     * Poistaa alustan käyttäjältä.
     *
     * @param toBeRemoved Poistettava alusta
     *
     * @return palauttaa true, jos alusta on poistettu käyttäjältä. Palauttaa
     * false, jos tapahtuu SQLException.
     *
     * @see PlatformDao#delete(app.gcdb.domain.Platform)
     */
    @Override
    public boolean delete(Platform toBeRemoved) {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM UserPlatform "
                    + "WHERE user_id = ? AND platform_id = ?");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, toBeRemoved.getId());
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    /**
     * Tarkastaa onko pelialusta listattu jo käyttäjälle.
     *
     * @param pltfrm Tarkastettava alusta
     *
     * @return palauttaa true, jos alusta on jo valmiiksi tietokannassa.
     * Palauttaa false, jos käyttäjälle ei ole listattu alustaa.
     *
     * @see PlatformDao#delete(app.gcdb.domain.Platform)
     */
    public boolean findIfAlreadyListed(Platform pltfrm) {
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

    /**
     * Tarkastaa onko yhdelläkään käyttäjällä pelialustaa listattuna.
     *
     * @param pltfrm Tarkastettava alusta
     *
     * @return palauttaa true, jos yhdelläkään käyttäjälle ei ole alustaa
     * tietokannassa. Palauttaa false, jos alusta on listattuna jollekin
     * käyttäjälle.
     * 
     * @throws SQLException virhe tietokannanhallinnassa
     *
     * @see PlatformDao#delete(app.gcdb.domain.Platform)
     */
    public boolean noUserHasPlatform(Platform pltfrm) throws SQLException {
        try (Connection connection = db.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM UserPlatform WHERE platform_id = ?");
            stmt.setInt(1, pltfrm.getId());
            ResultSet rsset = stmt.executeQuery();
            if (!rsset.next()) {
                rsset.close();
                connection.close();
                return true;
            }
            rsset.close();
            connection.close();
            return false;
        }
    }

    @Override
    public Platform findOne(Platform arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
