/**
 * GcdbService vastaa käyttöliittymälogiikasta.
 * Gcdb-luokka tuntee kulloinkin sisäänkirjautuneen käyttäjän ja valitun pelialustan.
 * Gcdb hallinnoi DAO-rajapinnasta johdettujen tietokantaluokkien työtä ja palauttaa
 * sisällön GUI:lle.
 */
package app.gcdb.domain;

import app.gcdb.dao.GameDao;
import app.gcdb.dao.PlatformDao;
import app.gcdb.dao.UserDao;
import app.gcdb.database.Database;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GcdbService {

    private User loggedInUser;
    private Platform currentlySelectedPlatform;
    private GameDao gameDao;
    private PlatformDao platformDao;
    private UserDao userDao;
    private Database database;

    public GcdbService() {
        this.database = new Database("jdbc:sqlite::resource:gcdb.db");
        this.userDao = new UserDao(database);
    }

    public GcdbService(Database database) {
        this.database = database;
        this.userDao = new UserDao(database);
    }

    public List<String> getPlatformsGames(String platForm) {
        currentlySelectedPlatform = loggedInUser.getOneOfUsersPlatforms(platForm);
        try {
            loggedInUser.setGames(gameDao.findAll(null));
        } catch (SQLException error) {
        }
        return loggedInUser.getViewableGames(currentlySelectedPlatform);
    }

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
            loggedInUser.setPlatforms(platformDao.findAll(null));
            return true;
        } catch (SQLException error) {
            return false;
        }
    }

    public int createNewUser(User user) {
        if (user.getUsername().equals("") || user.getPassHash() == 0) {
            return 1;
        } else if (user.getUsername().length() < 5 || user.getPassHash() == 0) {
            return 1;
        }
        boolean checkIfInDb = false;
        try {
            checkIfInDb = userDao.save(user);
        } catch (SQLException error) {
        }
        if (!checkIfInDb) {
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

    public int loggedInUsersGameCountForPlatform() {
        return loggedInUser.getViewableGames(currentlySelectedPlatform).size();
    }

    public String getLoggedInUser() {
        return loggedInUser.getUsername();
    }
    
    public void setCurrentlySelectedPlatform(Platform platform) {
        this.currentlySelectedPlatform = platform;
    }

    public List<String> deletePlatform(Platform platform) {
        try {
            platformDao.delete(platform);
            loggedInUser.setPlatforms(platformDao.findAll(null));
            return loggedInUser.getViewablePlatforms();
        } catch (SQLException error) {
        }
        return null;
    }

    public List<String> saveNewPlatform(Platform platform) {
        if (!platform.getName().isEmpty()) {
            try {
                platformDao.save(platform);
                loggedInUser.setPlatforms(platformDao.findAll(null));
            } catch (SQLException error) {
            }
            return loggedInUser.getViewablePlatforms();
        }
        return null;
    }

    public List<String> saveNewGame(Game game) {
        game.setPlatform(currentlySelectedPlatform.getId());
        if (!game.getName().isEmpty()) {
            try {
                gameDao.save(game);
                loggedInUser.setGames(gameDao.findAll(null));
            } catch (SQLException error) {
            }
        }
        List<String> gameList = loggedInUser.getViewableGames(currentlySelectedPlatform);
        return gameList;
    }

    public List<String> deleteGame(Game game) {
        try {
            Game toBeDeleted = loggedInUser.getGamesByPlatform(currentlySelectedPlatform).get(game.getId());
            gameDao.delete(toBeDeleted);
            loggedInUser.setGames(gameDao.findAll(null));
            return loggedInUser.getViewableGames(currentlySelectedPlatform);
        } catch (SQLException error) {
        }
        return null;
    }

    public boolean platformIsSelected() {
        return this.currentlySelectedPlatform != null;
    }

    public Game getGameByIndex(int index) {
        if (index < 0) {
            return null;
        }
        List<Game> byPlatform = loggedInUser.getGamesByPlatform(currentlySelectedPlatform);
        return byPlatform.get(index);
    }

    public boolean stop() {
        try {
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.out.println("error closing database");
        }
        return false;
    }

    public int conditionToInt(String condition) {
        switch (condition) {
            case "Not defined":
                return 1;
            case "Very Poor":
                return 2;
            case "Poor":
                return 3;
            case "Fair":
                return 4;
            case "Good":
                return 5;
            case "Very good":
                return 6;
            case "Excellent":
                return 7;
            case "Near mint":
                return 8;
            case "Mint":
                return 9;
            case "Gem mint":
                return 10;
        }
        return 1;
    }

    public String intToCondition(int cnd) {
        switch (cnd) {
            case 1:
                return "Not defined";
            case 2:
                return "Very Poor";
            case 3:
                return "Poor";
            case 4:
                return "Fair";
            case 5:
                return "Good";
            case 6:
                return "Very good";
            case 7:
                return "Excellent";
            case 8:
                return "Near mint";
            case 9:
                return "Mint";
            case 10:
                return "Gem mint";
        }
        return "Not defined";
    }

    public int contentToInt(String condition) {
        switch (condition) {
            case "Not defined":
                return 1;
            case "C":
                return 2;
            case "CI":
                return 3;
            case "CB":
                return 4;
            case "CIB":
                return 5;
            case "NIB":
                return 6;
        }
        return 1;
    }

    public String intToContent(int cnt) {
        switch (cnt) {
            case 1:
                return "Not defined";
            case 2:
                return "C";
            case 3:
                return "CI";
            case 4:
                return "CB";
            case 5:
                return "CIB";
            case 6:
                return "NIB";
        }
        return "Not defined";
    }
}
