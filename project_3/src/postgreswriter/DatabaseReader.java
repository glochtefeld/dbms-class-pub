package postgreswriter;
/**
 * @author Gavin Lochtefeld
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseReader {
    private Connection db_connection;
    private final String SQLITEDBPATH = "jdbc:sqlite:data/mlb/mlb.sqlite";
    
    public DatabaseReader() { }
    /**
     * Connect to a database (file)
     */
    public void connect() {
        try {
            this.db_connection = DriverManager.getConnection(SQLITEDBPATH);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    /**
     * Disconnect from a database (file)
     */
    public void disconnect() {
        try {
            this.db_connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }    
}