
import app.gcdb.domain.Game;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    User testUser;
    User testUser2;
    Platform testPlatform;
    Game testGame;

    @Before
    public void setUp() {
        this.testUser = new User("Erkki", "YsiViisEiUnohdu", 1);
        this.testUser2 = new User("Japi", 123456789, 1);
        testPlatform = new Platform("NES", 0);
        testGame = new Game("SMB3", testPlatform, Game.Condition.MINT, Game.Content.CIB, 0, "");
    }

    @Test
    public void generateAndGetPassHashWorks() {
        int testHash = "YsiViisEiUnohdu".hashCode();
        assertEquals(testUser.getPassHash(), testHash);
    }
    
    @Test
    public void setAndGetPlatformsWork() {
        List<String> testList = new ArrayList<>();
        testList.add(testPlatform.getName());
        testUser.setPlatforms(testList);
        assertEquals(testUser.getPlatforms(), testList);
    }

    @Test
    public void getUsernameTest() {
        assertEquals(testUser.getUsername(), "Erkki");
    }

    @Test
    public void getIdTest() {
        assertEquals(testUser.getId(), 1);
    }

    @Test
    public void addingGameWorks() {
        testUser.addGame(testGame);
        Game testGame2 = testUser.getGames().get(0);
        assertEquals(testGame2, testGame);
    }

    @Test
    public void removingGameWorks() {
        testUser.addGame(testGame);
        assertEquals(testUser.removeGame(testGame), true);
    }
    
    @Test
    public void removingGameThatDoesNotExistWorks() {
        assertFalse(testUser.removeGame(testGame));
    }
    
    @Test
    public void toStringWorks() {
        assertEquals(testUser.toString(), "Erkki 1164756051 1");
    }

}
