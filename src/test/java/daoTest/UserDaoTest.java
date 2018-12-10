package daoTest;

import app.gcdb.dao.UserDao;
import app.gcdb.database.Database;
import app.gcdb.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserDaoTest {

    private Database db;
    private User testUser;
    private User testUser2;
    private UserDao testDao;
    private Connection connection;

    public UserDaoTest() {
    }

    @Before
    public void setUp() throws SQLException {
        this.db = new Database("jdbc:sqlite::resource:testdb.db");
        this.testUser = new User("JohnDoe", 1164756051, 1);
        this.testUser2 = new User("Martti", 1132322211, 2);
        this.testDao = new UserDao(db);
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
    public void UserIsSavedToTheDatabase() throws SQLException{
        testDao.save(testUser);
        assertEquals(testUser.getUsername(), testDao.findOne("JohnDoe").getUsername());
    }
    
        @Test
    public void UsernameTakenUserIsNotSavedToTheDatabase() throws SQLException{
        testDao.save(testUser);
        assertEquals(false, testDao.save(testUser));
    }
}
