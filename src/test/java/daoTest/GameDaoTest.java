package daoTest;

import app.gcdb.dao.GameDao;
import app.gcdb.database.Database;
import app.gcdb.domain.Game;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameDaoTest {

    private Database database;
    private Connection connection;
    private GameDao testDao;
    private Game testGame;
    private Game testGame2;
    private Game testGame3;
    private User testUser;

    public GameDaoTest() {
    }

    @Before
    public void setUp() {
        this.database = new Database("jdbc:sqlite::resource:testdb.db"); // testdb.db = copy of gcdb.db
        this.testGame = new Game("The Legend of Zelda", 1, 4, 5, 0, "PAL", "");
        this.testGame2 = new Game("The Legend of Zelda", 1, 4, 5, 0, "NTSC", "");
        this.testGame3 = new Game("Yoshi's Cookie", 1, 4, 5, 0, "", "");
        this.testUser = new User("JohnDoe", 1164756051, 1);
        testDao = new GameDao(database, testUser);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = database.newConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Game");
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        }
        this.database.closeConnection();
    }

    @Test
    public void findOneReturnsCorrectGame() throws SQLException {
        testDao.save(testGame);
        testDao.save(testGame2);
        assertEquals("PAL", testDao.findOne(testGame).getRegion());
    }

    @Test
    public void findOneReturnsNullIfNotFound() throws SQLException {
        assertEquals(null, testDao.findOne(testGame));
    }
    
        @Test
    public void GameAlreadyInDatabaseNotSaved() throws SQLException {
        testDao.save(testGame);
        assertEquals(false, testDao.save(testGame));
    }
}
