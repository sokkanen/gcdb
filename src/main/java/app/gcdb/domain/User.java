package app.gcdb.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private int passHash;
    private List<Game> games;
    private List<String> platforms;
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

    public int generatePassHash(String passWord) {
        return passWord.hashCode();
    }

    public String getUsername() {
        return username;
    }

    public int getPassHash() {
        return passHash;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public boolean removeGame(Game game) {
        if (this.games.contains(game)) {
            this.games.remove(game);
            return true;
        } else {
            return false;
        }
    }

    public void setPlatforms(List<String> lst) {
        this.platforms = lst;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public List<String> getPlatforms() {
        return this.platforms;
    }

    @Override
    public String toString() {
        return this.username + " " + this.passHash + " " + this.id;
    }

    public int getId() {
        return this.id;
    }
}
