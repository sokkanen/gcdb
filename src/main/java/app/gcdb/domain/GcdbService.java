package app.gcdb.domain;

import app.gcdb.dao.GameDao;
import app.gcdb.dao.PlatformDao;
import app.gcdb.dao.UserDao;
import app.gcdb.database.Database;
import static app.gcdb.domain.GameCondition.values;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vastaa käyttöliittymälogiikasta. Luokka tuntee kulloinkin sisäänkirjautuneen
 * käyttäjän ja valitun pelialustan. Luokka hallinnoi DAO-rajapinnasta
 * johdettujen tietokantaluokkien työtä. Luokka palauttaa sisällön GUI:lle ja
 * toteuttaa GUI:n tarvitsemaa käyttöliittymälogiikkaa.
 */
public class GcdbService {

    private User loggedInUser;
    private Platform currentlySelectedPlatform;
    private GameDao gameDao;
    private PlatformDao platformDao;
    private UserDao userDao;
    private Database database;
    private String dbAddress;

    public GcdbService() {
        try {
            getDatabaseAddress();
        } catch (IOException ex) {
        }
        this.database = new Database("jdbc:sqlite:" + dbAddress);
        this.userDao = new UserDao(database);
    }

    /**
     * Konstruktori testien käyttöön.
     * 
     * @param database Testeissä käytettävä tietokanta
     */
    public GcdbService(Database database) {
        this.database = database;
        this.userDao = new UserDao(database);
    }

    /**
     * Lukee gcdb.conf -tiedostosta tietokannan osoitteen. Mikäli tiedostoa ei
     * löydy tai osoitetta ei ole, asettaa ohjelma tietokannaksi
     * /resources-kansiosta löytyvän oletustietokannan.
     * 
     * @throws IOException Virhe luettaessa asetustiedostoa
     *
     */
    public void getDatabaseAddress() throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("gcdb.conf"));
        } catch (FileNotFoundException ex) {
            dbAddress = ":resource:gcdb.db";
        }
        dbAddress = props.getProperty("database");
        if (dbAddress.equals("")) {
            dbAddress = ":resource:gcdb.db";
        }
    }

    /**
     * Palauttaa parametrinä annetun alustan pelit tietokannasta. Käytetään
     * palauttamaan GUI:lle ja kulloinkin sisäänkirjautuneelle käyttäjälle
     * valittuna olevan pelialustan pelit.
     *
     * @param platForm haettava pelialusta
     *
     * @return Palauttaa pelilistan GUI:lle.
     *
     * @see User#getOneOfUsersPlatforms(java.lang.String)
     * @see User#setGames(java.util.List)
     */
    public List<String> getPlatformsGames(String platForm) {
        currentlySelectedPlatform = loggedInUser.getOneOfUsersPlatforms(platForm);
        try {
            loggedInUser.setGames(gameDao.findAll());
        } catch (SQLException error) {
        }
        return loggedInUser.getViewableGames(currentlySelectedPlatform);
    }

    /**
     * Huolehtii käyttäjän sisäänkirjauksesta. Sisäänkirjauksen yhteydessä
     * luodaan sisäänkirjautuneelle käyttäjälle gameDao- ja platformDao-oliot
     * huolehtimaan alustojen ja pelien hallinnasta tietokantaan ja
     * tietokannasta.
     *
     * @param user sisäänkirjautumisen yhteydessä annetusta käyttäjätunnuksesta
     * ja salasanasta luotu käyttäjä.
     *
     * @return palauttaa false, jos käyttäjänimeä ei ole tietokannassa tai
     * salasana ei täsmää. Muutoin hakee kirjautuneelle käyttäjälle pelialustat
     * ja palauttaa true.
     *
     * @see User#setPlatforms(java.util.List)
     */
    public boolean logIn(User user) {
        try {
            User userFromDb = userDao.findOne(user.getUsername());
            if (userFromDb == null) {
                return false;
            } else if (userFromDb.getPassHash() != user.getPassHash()) {
                return false;
            }
            this.loggedInUser = userFromDb;
            this.platformDao = new PlatformDao(database, loggedInUser);
            this.gameDao = new GameDao(database, loggedInUser);
            loggedInUser.setPlatforms(platformDao.findAll());
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    /**
     * Luo uuden käyttäjän ja tallentaa sen tietokantaan.
     *
     * @param user sisäänkirjautumisen yhteydessä annetusta käyttäjätunnuksesta
     * ja salasanasta luotu käyttäjä.
     *
     * @return palauttaa 1 jos käyttäjänimi tai salasana on tyhjä tai jos
     * käyttäjätunnuksen tai salasanan pituus on alle 5 merkkiä. Palauttaa 2 jos
     * käyttäjätunnus on varattuna ja 3 jos uuden käyttäjän tallentaminen
     * tietokantaan onnistui.
     */
    public int createNewUser(User user) {
        if (user.getUsername().length() < 5 || user.getPassHash() == 0) {
            return 1;
        }
        boolean checkIfInDb = false;
        try {
            checkIfInDb = userDao.save(user);
        } catch (SQLException error) {
        }
        if (!checkIfInDb || user.getUsername().length() > 20) {
            return 2;
        } else {
            return 3;
        }
    }

    public List<String> getUsersPlatformList() {
        return this.loggedInUser.getViewablePlatforms();
    }

    public Platform getCurrentlySelectedPlatform() {
        return currentlySelectedPlatform;
    }

    /**
     * Palauttaa kirjautuneen käyttäjän pelien määrän valitulta alustalta.
     *
     * @return palauttaa GUI:lle sisäänkirjautuneen käyttäjän valittuna olevan
     * alustan pelit String-listana.
     */
    public int loggedInUsersGameCountForPlatform() {
        return loggedInUser.getViewableGames(currentlySelectedPlatform).size();
    }

    public String getLoggedInUser() {
        return loggedInUser.getUsername();
    }

    public void setCurrentlySelectedPlatform(Platform platform) {
        this.currentlySelectedPlatform = platform;
    }

    /**
     * Poistaa parametrina annetun alustan käyttäjältä.
     *
     * @param platform poistettava alusta.
     *
     * @return palauttaa GUI:lle käyttäjän uuden alustalistan tai poiston
     * epäonnistuttua null.
     */
    public List<String> deletePlatform(Platform platform) {
        try {
            platformDao.delete(platform);
            loggedInUser.setPlatforms(platformDao.findAll());
            return loggedInUser.getViewablePlatforms();
        } catch (SQLException error) {
        }
        return null;
    }

    /**
     * Tallentaa parametrina annetun alustan käyttäjälle.
     *
     * @param platform lisättävä alusta.
     *
     * @return palauttaa GUI:lle käyttäjän uuden alustalistan tai poiston
     * epäonnistuttua null.
     */
    public List<String> saveNewPlatform(Platform platform) {
        if (!platform.getName().isEmpty()) {
            try {
                platformDao.save(platform);
                loggedInUser.setPlatforms(platformDao.findAll());
            } catch (SQLException error) {
            }
            return loggedInUser.getViewablePlatforms();
        }
        return null;
    }

    /**
     * Tallentaa parametrina annetun pelin käyttäjälle.
     *
     * @param game lisättävä peli.
     *
     * @return palauttaa GUI:lle käyttäjän uuden pelilistan.
     *
     */
    public List<String> saveNewGame(Game game) {
        game.setPlatform(currentlySelectedPlatform.getId());
        if (!game.getName().isEmpty()) {
            try {
                gameDao.save(game);
                loggedInUser.setGames(gameDao.findAll());
            } catch (SQLException error) {
            }
        }
        List<String> gameList = loggedInUser.getViewableGames(currentlySelectedPlatform);
        return gameList;
    }

    /**
     * Päivittää parametrina annetun pelin käyttäjälle. Käyttää deleteGame ja
     * saveNewGame -metodeja.
     *
     * @param game Päivitettävä peli.
     *
     * @see GcdbService#deleteGame(app.gcdb.domain.Game)
     * @see GcdbService#saveNewGame(app.gcdb.domain.Game)
     *
     * @return palauttaa GUI:lle käyttäjän uuden pelilistan.
     *
     */
    public List<String> updateGame(Game game) {
        deleteGame(game);
        return saveNewGame(game);
    }

    /**
     * Poistaa parametrina annetun pelin käyttäjältä. Jos peli poistetaan
     * listaindeksin perusteella, on pelin nimi tyhjä. Mikäli poisto tapahtuu
     * muokkaukseen liittyen, on parametrina annettavalla pelillä nimi.
     *
     * @param game poistettava peli.
     *
     * @return palauttaa GUI:lle käyttäjän uuden pelilistan tai poiston
     * epäonnistuessa null.
     *
     */
    public List<String> deleteGame(Game game) {
        try {
            if (game.getName().isEmpty()) {
                Game toBeDeleted = loggedInUser.getGamesByPlatform(currentlySelectedPlatform).get(game.getId());
                gameDao.delete(toBeDeleted);
            } else {
                gameDao.delete(game);
            }
            loggedInUser.setGames(gameDao.findAll());
            return loggedInUser.getViewableGames(currentlySelectedPlatform);
        } catch (SQLException error) {
        }
        return null;
    }

    /**
     * Poistaa parametrina annetun alustan kaikki pelit käyttäjältä. Metodia
     * käytetään poistettaessa alusta, jolle on lisätty pelejä.
     *
     * @param platform alusta, jolta kaikki pelit poistetaan.
     *
     */
    public void deleteGamesByPlatform(Platform platform) {
        List<Game> gamesToBeDeleted = this.loggedInUser.getGamesByPlatform(platform);
        for (int i = 0; i < gamesToBeDeleted.size(); i++) {
            try {
                gameDao.delete(gamesToBeDeleted.get(i));
            } catch (SQLException ex) {
            }
        }
    }

    public boolean platformIsSelected() {
        return this.currentlySelectedPlatform != null;
    }

    /**
     * Palauttaa käyttäjältä parametrin mukaisen pelin.
     *
     * @param index haettavan pelin indeksi.
     *
     * @return Palauttaa indeksin mukaisen pelin tai indeksin ollessa pienempi
     * kuin 0 (tyhjä ListView GUI:lla), palauttaa null.
     *
     */
    public Game getGameByIndex(int index) {
        if (index < 0) {
            return null;
        }
        List<Game> byPlatform = loggedInUser.getGamesByPlatform(currentlySelectedPlatform);
        return byPlatform.get(index);
    }

    /**
     * Palauttaa pelin aluekoodin / alueen.
     *
     * @param game Haettava peli.
     *
     * @return Palauttaa pelin aluekoodin tai U/K, kun aluekoodia ei ole
     * määritelty.
     *
     */
    public String getGameRegion(Game game) {
        if (game.getRegion().equals("")) {
            return "U/K";
        }
        return game.getRegion();
    }

    /**
     * Palauttaa kaikki kuntoluokitusta kuvaavan GameCondition-luokan
     * merkkijonoarvot listana
     *
     * @return Palauttaa merkkijonolistan.
     */
    public List<String> getConditions() {
        List<String> conditions = new ArrayList<>();
        for (GameCondition cnd : values()) {
            conditions.add(cnd.getCndStr());
        }
        return conditions;
    }

    /**
     * Palauttaa pelin kuntoa kuvaavan luvun parametrina annetun merkkijonon
     * perusteella.
     *
     * @param condition kuntoluokkaa vastaava merkkijono
     *
     * @return Palauttaa kuntoluokkaa vastaavan luvun
     */
    public int conditionToInt(String condition) {
        for (GameCondition cnd : values()) {
            if (cnd.getCndStr().equals(condition)) {
                return cnd.getCndNr();
            }
        }
        return 0;
    }

    /**
     * Palauttaa pelin kuntoa kuvaavan merkkijonon parametrina annetun numeron
     * perusteella.
     *
     * @param paramInt kuntoluokkaa vastaava luku
     *
     * @return Palauttaa kuntoluokkaa vastaavan merkkijonon
     */
    public String intToCondition(int paramInt) {
        for (GameCondition cnd : values()) {
            if (cnd.getCndNr() == paramInt) {
                return cnd.getCndStr();
            }
        }
        return "Not Defined";
    }

    /**
     * Palauttaa kaikki sisältöä kuvaavan GameContent-luokan merkkijonoarvot
     * listana
     *
     * @return Palauttaa merkkijonolistan.
     */
    public List<String> getContents() {
        List<String> contents = new ArrayList<>();
        for (GameContent cnt : GameContent.values()) {
            contents.add(cnt.getCntStr());
        }
        return contents;
    }

    /**
     * Palauttaa pelin sisältöä kuvaavan luvun parametrina annetun merkkijonon
     * perusteella.
     *
     * @param content sisältöä vastaava merkkijono
     *
     * @return Palauttaa sisältöä vastaavan luvun
     */
    public int contentToInt(String content) {
        for (GameContent cnt : GameContent.values()) {
            if (cnt.getCntStr().equals(content)) {
                return cnt.getCntNr();
            }
        }
        return 0;
    }

    /**
     * Palauttaa pelin sisältöä kuvaavan merkkijonon parametrina annetun numeron
     * perusteella.
     *
     * @param paramInt sisältöä vastaava luku
     *
     * @return Palauttaa sisältöä vastaavan merkkijonon
     */
    public String intToContent(int paramInt) {
        for (GameContent cnt : GameContent.values()) {
            if (cnt.getCntNr() == paramInt) {
                return cnt.getCntStr();
            }
        }
        return "Not Defined";
    }

    /**
     * Katkaisee tietokantayhteyden.
     *
     * @return Palauttaa true, kun yhteys on katkaistu. Muussa tapauksessa
     * palauttaa false.
     *
     */
    public boolean stop() {
        try {
            database.closeConnection();
            return true;
        } catch (SQLException e) {
        }
        return false;
    }
}
