package daoTest;

import app.gcdb.dao.PlatformDao;
import app.gcdb.database.Database;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PlatformDaoTest {

    private Database db;
    private User testUser;
    private PlatformDao testDao;
    private Platform testPlatform;
    private Platform testPlatform2;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        this.db = new Database("jdbc:sqlite::resource:testdb.db");
        this.testUser = new User("JohnDoe", 1164756051, 1);
        this.testDao = new PlatformDao(db, testUser);
        this.testPlatform = new Platform("NES", 1);
        this.testPlatform2 = new Platform("WII", 2);
        this.connection = db.newConnection();
    }

    @After
    public void tearDown() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM UserPlatform");
        stmt.executeUpdate();
        stmt.close();
        stmt = connection.prepareStatement("DELETE FROM Platform");
        stmt.executeUpdate();
        stmt.close();
        stmt = connection.prepareStatement("DELETE FROM User");
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    @Test
    public void cannotSavePlatformThatExists() throws SQLException {
        testDao.save(testPlatform);
        assertEquals(testDao.save(testPlatform), false);
    }

    @Test
    public void twoDifferentPlatformsCanBeSaved() throws SQLException {
        testDao.save(testPlatform);
        assertEquals(testDao.save(testPlatform2), true);
    }
}
