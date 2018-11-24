import app.gcdb.dao.PlatformDao;
import app.gcdb.database.Database;
import app.gcdb.domain.User;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlatformDaoTest {
    
    private Database db;
    private User testUser;
    private PlatformDao testDao;
    
    @Before
    public void setUp() {
        this.db = new Database("jdbc:sqlite:testdb.db");
        this.testUser = new User("JohnDoe", 1164756051, 0);
        this.testDao = new PlatformDao(db, testUser);
    }
    
    @Test
    public void cannotSavePlatformThatExists() throws SQLException{
        
    }
    
}
