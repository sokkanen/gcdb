package app.gcdb.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private int passHash;
    private List<Game> games;
    private List<Platform> platforms;

    public User(String username, String password) {
        this.username = username;
        this.passHash = generatePassHash(password);
    }
    
    public User(String username, int passhash) {
        this.username = username;
        this.passHash = passhash;
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
        try {
            this.games.remove(game);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void platform(Platform platform) {
        this.platforms.add(platform);
    }

    public boolean removePlatform(Platform platform) {
        try {
            this.platforms.remove(platform);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Game> getGames() {
        return this.games;
    }

    public List<Platform> getPlatforms() {
        return this.platforms;
    }
    
    @Override
    public String toString(){
        return this.username + " " + this.passHash;
    }
}
