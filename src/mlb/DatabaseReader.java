package mlb;
/**
 * @author Roman Yasinovskyy
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
            Logger.getLogger(DatabaseReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Disconnect from a database (file)
     */
    public void disconnect() {
        try {
            this.db_connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Populate the list of divisions
     * @param divisions
     */
    public void getDivisions(ArrayList<String> divisions) {
        Statement stat;
        ResultSet results;
        
        this.connect();
        try {
            stat = this.db_connection.createStatement();
            // TODO: Write an SQL statement to retrieve a league (conference) and a division
            String sql = "SELECT DISTINCT conference, division FROM team";
            // TODO: Add all 6 combinations to the ArrayList divisions
            results = stat.executeQuery(sql);
            
            while (results.next()) {
                divisions.add(results.getString("conference") + " | " + results.getString("division"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }
    /**
     * Read all teams from the database
     * @param confDiv
     * @param teams
     */
    public void getTeams(String confDiv, ArrayList<String> teams) {
        Statement stat;
        ResultSet results;
        String conference = confDiv.split(" | ")[0];
        String division = confDiv.split(" | ")[2];
        
        this.connect();
        try {
            stat = this.db_connection.createStatement();
            // TODO: Write an SQL statement to retrieve a teams from a specific division
            String sql = String.format("SELECT name FROM team "
                    + "WHERE division='%s' AND conference='%s'",division,conference);
            results = stat.executeQuery(sql);
            // TODO: Add all 5 teams to the ArrayList teams
            while (results.next()) {
                teams.add(results.getString("name"));
            }
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }
    /**
     * @param teamName
     * @return Team info
     */
    public Team getTeamInfo(String teamName) {
        Team team = null;
        ArrayList<Player> roster = new ArrayList<>();
        Address address = null;
        Statement statement;
        ResultSet results;
        
        // TODO: Retrieve team info (roster, address, and logo) from the database
        this.connect();
        try {
            statement = this.db_connection.createStatement();
            String sql = String.format("SELECT "
                    + "team.id AS TeamID, team.*, player.id as PlayerID, player.*, address.* "
                    + "FROM team "
                    + "INNER JOIN player ON team.name = player.team "
                    + "INNER JOIN address ON team.name = address.team "
                    + "WHERE team.name = '%s'",teamName);
            results = statement.executeQuery(sql);
            
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.INFO,"Team exists");
            team = new Team(results.getString("TeamID"),
                results.getString("abbr"),
                teamName,
                results.getString("conference"),
                results.getString("division"));

            team.setLogo(results.getBytes("logo"));
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.INFO, "Address exists");
            address = new Address(
                    results.getString("team"),
                    results.getString("site"),
                    results.getString("street"),
                    results.getString("city"),
                    results.getString("state"),
                    results.getString("zip"),
                    results.getString("phone"),
                    results.getString("url"));
            
            while (results.next()) {
                roster.add(
                        new Player(
                                results.getString("PlayerID"),
                                results.getString("name"),
                                results.getString("team"),
                                results.getString("position")
                        )
                );
            }
            if (address != null && team != null)
                team.setAddress(address);
            if (team != null)
                team.setRoster(roster);
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        
        return team;
    }
}