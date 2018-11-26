package app.gcdb.domain;

import java.util.ArrayList;
import java.util.List;

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

    public Platform getOneOfUsersPlatforms(String searchstring) {
        for (int i = 0; i < this.platforms.size(); i++) {
            if (this.platforms.get(i).getName().equals(searchstring)) {
                return this.platforms.get(i);
            }
        }
        return null;
    }

    public void setPlatforms(List<Platform> lst) {
        this.platforms = lst;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public List<Platform> getPlatforms() {
        return this.platforms;
    }

    public List<String> getViewablePlatforms() {
        List<String> platformStrings = new ArrayList<>();
        for (int i = 0; i < this.platforms.size(); i++) {
            platformStrings.add(this.platforms.get(i).getName());
        }
        return platformStrings;
    }

    @Override
    public String toString() {
        return this.username + " " + this.passHash + " " + this.id;
    }

    public int getId() {
        return this.id;
    }
}
