package app.gcdb.domain;

import app.gcdb.dao.GameDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User-luokalla kuvataan ohjelman käyttäjiä.
 */
public class User {

    private String username;
    private int passHash;
    private List<Game> games;
    private List<Platform> platforms;
    private int id;

    public User(String username, String password, int id) {
        this.id = id;
        this.username = username;
        this.passHash = generatePassHash(password);
        this.platforms = new ArrayList<>();
        this.games = new ArrayList<>();
    }

    public User(String username, int passhash, int id) {
        this.username = username;
        this.passHash = passhash;
        this.id = id;
        this.platforms = new ArrayList<>();
        this.games = new ArrayList<>();
    }

    public User(String username, String password, List<Game> games, List<Platform> platforms) {
        this.username = username;
        this.passHash = generatePassHash(password);
        this.games = new ArrayList<>();
        this.platforms = new ArrayList<>();
    }

    /**
     * Metodi luo annetusta salasanasta hajautusarvon. Käyttäjien salasanat
     * tallennetaan tietokantaan hajautusarvoina.
     *
     * @param passWord käyttäjän antama salasana.
     *
     * @return hajautusarvo(int)
     */
    public int generatePassHash(String passWord) {
        if (passWord.length() > 4) {
            return passWord.hashCode();
        }
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public int getPassHash() {
        return passHash;
    }

    /**
     * Metodi palauttaa käyttäjän pelialustan.
     *
     * @param searchstring haetun pelialustan nimi.
     *
     * @return pyydetty pelialusta, jos ei löydy, niin null
     */
    public Platform getOneOfUsersPlatforms(String searchstring) {
        for (int i = 0; i < this.platforms.size(); i++) {
            if (this.platforms.get(i).getName().equals(searchstring)) {
                return this.platforms.get(i);
            }
        }
        return null;
    }

    /**
     * Metodi palauttaa yhden pelin.
     *
     * @param index haetun pelin indeksi.
     *
     * @return pyydetty peli.
     */
    public Game getOneOfUsersGames(int index) {
        return this.games.get(index);
    }

    public void setPlatforms(List<Platform> lst) {
        this.platforms = lst;
    }

    /**
     * Metodi järjestää käyttäjän pelit aakkosjärjestykseen ja asettaa pelit
     * käyttäjän pelilistaksi.
     *
     * @param lst tietokannasta haettu käyttäjän pelilista.
     *
     * @see Game#compareTo(app.gcdb.domain.Game)
     */
    public void setGames(List<Game> lst) {
        Collections.sort(lst);
        this.games = lst;
    }

    public List<Game> getGames() {
        return this.games;
    }

    /**
     * Metodi palauttaa käyttäjän pelit pyydetyltä pelialustalta.
     *
     * @param platform Pelialusta, jolta pelit halutaan.
     *
     * @return Palauttaa pelilistan kyseiseltä käyttäjältä ja pelialustalta.
     */
    public List<Game> getGamesByPlatform(Platform platform) {
        List<Game> plGames = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getPlatform() == platform.getId()) {
                plGames.add(games.get(i));
            }
        }
        return plGames;
    }

    public List<Platform> getPlatforms() {
        return this.platforms;
    }

    /**
     * Metodi palauttaa käyttäjän pelialustojen nimet. Metodia käytetään
     * GUI-luokan ListView-olioiden parametrina.
     *
     * @return Käyttäjän pelialustat String-listana.
     */
    public List<String> getViewablePlatforms() {
        List<String> platformStrings = new ArrayList<>();
        for (int i = 0; i < this.platforms.size(); i++) {
            platformStrings.add(this.platforms.get(i).getName());
        }
        return platformStrings;
    }

    /**
     * Metodi palauttaa käyttäjän pelien nimet pyydetyltä alustalta. Metodia
     * käytetään GUI-luokan ListView-olioiden parametrina.
     *
     * @return Käyttäjän pelit String-listana.
     */
    public List<String> getViewableGames(Platform platform) {
        List<String> gameStrings = new ArrayList<>();
        for (int i = 0; i < this.games.size(); i++) {
            if (platform.getId() == this.games.get(i).getPlatform()) {
                gameStrings.add(this.games.get(i).getName());
            }
        }
        return gameStrings;
    }

    @Override
    public String toString() {
        return this.username + " " + this.passHash + " " + this.id;
    }

    public int getId() {
        return this.id;
    }
}
