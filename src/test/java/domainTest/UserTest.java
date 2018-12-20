package domainTest;


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
        testGame = new Game("SMB3", testPlatform.getId(), 9, 5, 0, "" , "");
    }

    @Test
    public void generateAndGetPassHashWorks() {
        int testHash = "YsiViisEiUnohdu".hashCode();
        assertEquals(testUser.getPassHash(), testHash);
    }

    @Test
    public void setAndGetPlatformsWork() {
        List<Platform> testList = new ArrayList<>();
        testList.add(testPlatform);
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
    public void toStringWorks() {
        assertEquals(testUser.toString(), "Erkki 1164756051 1");
    }

    @Test
    public void getOnePlatformWorks() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(testPlatform);
        testUser.setPlatforms(platforms);
        assertEquals(testUser.getOneOfUsersPlatforms("NES"), testPlatform);
    }

    @Test
    public void getOnePlatformThatDoesNotExistWorks() {
        List<Platform> platforms = new ArrayList<>();
        testUser.setPlatforms(platforms);
        assertEquals(testUser.getOneOfUsersPlatforms("NES"), null);
    }

    @Test
    public void getViewablePlatformsWorks() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(testPlatform);
        testUser.setPlatforms(platforms);
        assertEquals(testUser.getViewablePlatforms().get(0), "NES");
    }
}
