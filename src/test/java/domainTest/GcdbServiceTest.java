package domainTest;

import app.gcdb.database.Database;
import app.gcdb.domain.Game;
import app.gcdb.domain.GcdbService;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GcdbServiceTest {

    private Database database;
    private GcdbService gcdbService;
    private User testUser;
    private Platform testPlatform;
    private Game testGame;
    private Game testGame2;

    @Before
    public void setUp() {
        this.database = new Database("jdbc:sqlite::resource:testdb.db"); // testdb.db = copy of gcdb.db
        this.gcdbService = new GcdbService(database);
        this.testUser = new User("Jamppa", "Tuominen", 1);
        this.testPlatform = new Platform("NES", 1);
        this.testGame = new Game("The Legend of Zelda", 1, 4, 5, 0, "");
        this.testGame2 = new Game("Yoshi's Cookie", 1, 4, 5, 0, "");
        gcdbService.createNewUser(testUser);
        gcdbService.logIn(testUser);
        gcdbService.setCurrentlySelectedPlatform(testPlatform);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = database.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM UserPlatform");
            stmt.executeUpdate();
            stmt = connection.prepareStatement("DELETE FROM Game");
            stmt.executeUpdate();
            stmt = connection.prepareStatement("DELETE FROM Platform");
            stmt.executeUpdate();
            stmt = connection.prepareStatement("DELETE FROM User");
            stmt.executeUpdate();
            connection.close();
        }
        this.database.closeConnection();
    }

    @Test
    public void GetPlatformsReturnEmptyListWithNoGamesAdded() {
        gcdbService.saveNewPlatform(testPlatform);
        List<String> testList = gcdbService.getPlatformsGames(testPlatform.getName());
        assertEquals(0, testList.size());
    }

    @Test
    public void GetPlatformsReturnListWithOneGame() {
        gcdbService.saveNewPlatform(testPlatform);
        List<String> testList = gcdbService.saveNewGame(testGame);
        assertEquals("The Legend of Zelda", testList.get(0));
    }

    @Test
    public void UserIsFoundInDbAndSetAsLoggedInUser() {
        assertEquals(true, gcdbService.logIn(testUser));
    }

    @Test
    public void UsernameAlreadyExistsNoNewUserCreated() {
        assertEquals(2, gcdbService.createNewUser(testUser));
    }

    @Test
    public void UsernameTooShortNoNewUserCreated() {
        assertEquals(1, gcdbService.createNewUser(new User("Kake", "Moroo", 0)));
    }

    @Test
    public void ConditionsOkNewUserIsCreated() {
        assertEquals(3, gcdbService.createNewUser(new User("Jalmarsson", "Teremoro123", 0)));
    }

    @Test
    public void PlatformListEmptyAfterRemovingOnlyPlatform() {
        List<String> testList = gcdbService.deletePlatform(testPlatform);
        assertEquals(0, testList.size());
    }

    @Test
    public void saveNewGameAddsGameToUser() {
        gcdbService.saveNewGame(testGame);
        List<String> testList = gcdbService.saveNewGame(testGame2);
        assertEquals("Yoshi's Cookie", testList.get(1));
    }

    @Test
    public void saveNewPlatformAddsPlatformToUser() {
        gcdbService.saveNewPlatform(testPlatform);
        List<String> testList = gcdbService.saveNewPlatform(new Platform("SNES", 0));
        assertEquals("SNES", testList.get(1));
    }

    @Test
    public void DeleteGameDeletesGame() {
        gcdbService.saveNewPlatform(testPlatform);
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        List<String> testList = gcdbService.deleteGame(testGame);
        assertEquals("Yoshi's Cookie", testList.get(0));
    }

    @Test
    public void GetGameByIndexWorks() { // AKA sorting list works
        gcdbService.saveNewPlatform(testPlatform);
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        gcdbService.saveNewGame(new Game("Mega Man 5", 4, 2, ""));
        Game game = gcdbService.getGameByIndex(0);
        assertEquals("Mega Man 5", game.getName());
    }

    @Test
    public void StopTest() {
        assertEquals(true, gcdbService.stop());
    }
}
