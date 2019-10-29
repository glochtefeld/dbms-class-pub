package postgreswriter;
/**
 * @author Roman Yasinovskyy
 */
import postgreswriter.DatabaseReader;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseReaderTest {
    
    public DatabaseReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class DatabaseReader.
     */
    @Test
    public void testConnect() {
        System.out.println("connect");
        DatabaseReader instance = new DatabaseReader();
        instance.connect();
    }

    /**
     * Test of disconnect method, of class DatabaseReader.
     */
    @Test(expected = NullPointerException.class)
    public void testDisconnect() {
        System.out.println("disconnect");
        DatabaseReader instance = new DatabaseReader();
        instance.disconnect();
    }
    
}
