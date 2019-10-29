package postgreswriter;
/**
 * @author Roman Yasinovskyy
 */
import postgreswriter.DatabaseWriter;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseWriterTest {
    
    public DatabaseWriterTest() {
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
     * Test of createTables method, of class DatabaseWriter.
     */
    @Test
    public void testCreateTables() throws Exception {
        System.out.println("createTables");
        String db_filename = "test.sqlite";
        DatabaseWriter instance = new DatabaseWriter();
        instance.createTables(db_filename);
    }   
}
