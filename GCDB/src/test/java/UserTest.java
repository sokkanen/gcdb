import app.gcdb.domain.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    
    User testikayttaja;
    
    @Before
    public void setUp() {
        this.testikayttaja = new User("Erkki", "YsiViisEiUnohdu");
    }
}