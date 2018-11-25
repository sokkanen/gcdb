import app.gcdb.domain.Game;
import app.gcdb.domain.Platform;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.*;

public class GameTest {
    
    private Game testGame;
    private Game testGame2;
    private Platform platform;

    @Before
    public void setUp() {
        this.platform = new Platform("NES", 1);
        this.testGame = new Game("The Legend of Zelda", this.platform, Game.Condition.MINT, Game.Content.CIB, 0, "");
        this.testGame2 = new Game("Mr. Gimmick", this.platform, Game.Condition.NEAR_MINT, Game.Content.NIB ,1, "");
    }
    
    @Test
    public void compareWorksAsExpectedWhenNotSameGames(){
        assertNotEquals(0, testGame.compareTo(testGame2));
    }
    
    @Test
    public void compareWorksAsExpected(){
        assertThat(this.testGame.compareTo(testGame2),greaterThan(0));
    }
    
    @Test
    public void getNameTest(){
        assertEquals("Mr. Gimmick", testGame2.getName());
    }
}
