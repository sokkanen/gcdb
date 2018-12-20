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
    private Platform testPlatform2;
    private Game testGame;
    private Game testGame2;
    private Game testGame3;
    private Game testGameIndex;
    
    @Before
    public void setUp() {
        this.database = new Database("jdbc:sqlite::resource:testdb.db");
        this.gcdbService = new GcdbService(database);
        this.testUser = new User("Jamppa", "Tuominen", 1);
        this.testPlatform = new Platform("NES", 1);
        this.testPlatform2 = new Platform("PS1", 2);
        this.testGame = new Game("The Legend of Zelda", 1, 4, 5, 0, "", "");
        this.testGame2 = new Game("Yoshi's Cookie", 1, 4, 5, 1, "", "");
        this.testGame3 = new Game("Mega Man 5", 1, 4, 5, 2, "", "");
        this.testGameIndex = new Game(2);
        gcdbService.createNewUser(testUser);
        gcdbService.logIn(testUser);
        gcdbService.saveNewPlatform(testPlatform);
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
        List<String> testList = gcdbService.getPlatformsGames(testPlatform.getName());
        assertEquals(0, testList.size());
    }
    
    @Test
    public void GetPlatformsReturnListWithOneGame() {
        List<String> testList = gcdbService.saveNewGame(testGame);
        assertEquals("1. The Legend of Zelda", testList.get(0));
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
        assertEquals("2. Yoshi's Cookie", testList.get(1));
    }
    
    @Test
    public void saveNewPlatformAddsPlatformToUser() {
        List<String> testList = gcdbService.saveNewPlatform(new Platform("SNES", 0));
        assertEquals("SNES", testList.get(1));
    }
    
    @Test
    public void DeleteGameDeletesGameWithName() {
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        gcdbService.saveNewGame(testGame3);
        gcdbService.deleteGame(testGame2);
        List<String> testList = gcdbService.getPlatformsGames(testPlatform.getName());
        assertEquals(2, testList.size());
    }
    
    @Test
    public void DeleteGameDeletesGameWithIndex() {
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        gcdbService.saveNewGame(testGame3);
        gcdbService.deleteGame(testGameIndex);
        int count = gcdbService.loggedInUsersGameCountForPlatform();
        assertEquals(2, count);
    }
    
    @Test
    public void GetGameByIndexWorks() {
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        gcdbService.saveNewGame(new Game("Mega Man 5", 4, 2, "", ""));
        Game game = gcdbService.getGameByIndex(0);
        assertEquals("Mega Man 5", game.getName());
    }
    
    @Test
    public void UserIsNotFoundFromDatabase() {
        assertEquals(false, gcdbService.logIn(new User("FooBar", 123456789, 0)));
    }
    
    @Test
    public void WrongPasswordIsGivenUserNotLoggedIn() {
        assertEquals(false, gcdbService.logIn(new User("Jamppa", "Juominen", 1)));
    }
    
    @Test
    public void AllGamesByPlatformAreDeleted() {
        gcdbService.saveNewGame(testGame);
        gcdbService.saveNewGame(testGame2);
        gcdbService.deleteGamesByPlatform(testPlatform);
        assertEquals(0, gcdbService.getPlatformsGames(testPlatform.getName()).size());
    }
    
    @Test
    public void updateGameWorks() {
        gcdbService.saveNewGame(testGame);
        testGame.setComment("This is a comment");
        gcdbService.updateGame(testGame);
        Game comparable = gcdbService.getGameByIndex(1);
        assertEquals("This is a comment", comparable.getComment());
    }
    
    @Test
    public void integerToContentReturnsCorrectValue() {
        assertEquals("Cart with Instr.", gcdbService.intToContent(3));
    }
    
    @Test
    public void integerToConditionReturnsCorrectValue() {
        assertEquals("Poor", gcdbService.intToCondition(3));
    }
    
    @Test
    public void contentToIntegerReturnsCorrectValue() {
        assertEquals(3, gcdbService.contentToInt("Cart with Instr."));
    }
    
    @Test
    public void conditionToIntegerReturnsCorrectValue() {
        assertEquals(3, gcdbService.conditionToInt("Poor"));
    }
    
    @Test
    public void StopTest() {
        assertEquals(true, gcdbService.stop());
    }
}
