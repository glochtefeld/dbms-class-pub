package mlb;
/**
 * @author Roman Yasinovskyy
 */
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.List;

public class DatabaseWriter {
    public final String SQLITEDBPATH = "jdbc:sqlite:data/mlb/";
    /**
     * @param filename (JSON file)
     * @return League
     */
    public ArrayList<Team> readTeamFromJson(String filename) {
        ArrayList<Team> league = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;
        
        try {
            jsonParser = jsonFactory.createParser(new File(filename));
            jsonParser.nextToken();
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                Team team = mapper.readValue(jsonParser, Team.class);
                league.add(team);
            }
            jsonParser.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return league;
    }
    /**
     * @param filename (TXT file)
     * @return Addresses
     */
    public ArrayList<Address> readAddressFromTxt(String filename) {
        ArrayList<Address> addressBook = new ArrayList<>();
        try {
            Scanner fs = new Scanner(new File(filename));
            while (fs.hasNextLine()) {
                // TODO: Parse each line into an object of type Address and add it to the ArrayList
                String input = fs.nextLine();
                String[] splitLine = input.split("\\t");
                // data is stored: Name Park Address City State Zip Phone Web
                Address newAddr = new Address(
                        splitLine[0],
                        splitLine[1],
                        splitLine[2],
                        splitLine[3],
                        splitLine[4],
                        splitLine[5],
                        splitLine[6],
                        splitLine[7]);
                addressBook.add(newAddr);
            }
        } catch (IOException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return addressBook;
    }
    public ArrayList<Player> readPlayerFromCsv(String filename) {
        ArrayList<Player> roster = new ArrayList<>();
        
        // TODO: Read the CSV file, create an object of type Player from each line and add it to the ArrayList
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            List<String[]> allLines = reader.readAll();
            for (int i =1;i<allLines.size();i++) {
                String[] data = allLines.get(i);
                // relevant parts of csv:
                //   mlb_id, mlb_name, mlb_pos, mlb_team
                //   in positions 0,1,3,2
                Player player = new Player(
                        data[0],
                        data[1],
                        data[4],
                        data[2]);
                roster.add(player);
            }
        }
        catch (IOException ex) {
                Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE,null,ex);
                }
        return roster;
        
    }
    /**
     * Create tables cities and teams
     * @param db_filename
     * @throws SQLException 
     */
    public void createTables(String db_filename) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        Statement statement = db_connection.createStatement();
        
        statement.executeUpdate("DROP TABLE IF EXISTS team;");
        statement.executeUpdate("CREATE TABLE team ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "id TEXT NOT NULL,"
                            + "abbr TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "conference TEXT NOT NULL,"
                            + "division TEXT NOT NULL,"
                            + "logo BLOB);");
        
        statement.execute("PRAGMA foreign_keys = ON;");
        
        statement.executeUpdate("DROP TABLE IF EXISTS player;");
        statement.executeUpdate("CREATE TABLE player ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "id TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "team TEXT NOT NULL,"
                            + "position TEXT NOT NULL,"
                            + "FOREIGN KEY (team) REFERENCES team(idpk));");

        statement.executeUpdate("DROP TABLE IF EXISTS address;");
        statement.executeUpdate("CREATE TABLE address ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "team TEXT NOT NULL,"
                            + "site TEXT NOT NULL,"
                            + "street TEXT NOT NULL,"
                            + "city TEXT NOT NULL,"
                            + "state TEXT NOT NULL,"
                            + "zip TEXT NOT NULL,"
                            + "phone TEXT NOT NULL,"
                            + "url TEXT NOT NULL,"
                            + "FOREIGN KEY (team) REFERENCES team(idpk));");
        db_connection.close();
    }
   /**
     * Read the file and returns the byte array
     * @param filename
     * @return the bytes of the file
     */
    private byte[] readLogoFile(String filename) {
        ByteArrayOutputStream byteArrOutStream = null;
        try {
            File fileIn = new File(filename);
            FileInputStream fileInStream = new FileInputStream(fileIn);
            byte[] buffer = new byte[1024];
            byteArrOutStream = new ByteArrayOutputStream();
            for (int len; (len = fileInStream.read(buffer)) != -1;) {
                byteArrOutStream.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return byteArrOutStream != null ? byteArrOutStream.toByteArray() : null;
    }
    /**
     * @param db_filename
     * @param league 
     * @throws java.sql.SQLException 
     */
    public void writeTeamTable(String db_filename, ArrayList<Team> league) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        // TODO: Write an SQL statement to insert a new team into a table
        String sql = "INSERT INTO team "
                + "(id, abbr, name, conference, division, logo) "
                + "VALUES "
                + "(?,?,?,?,?,?)";
        for (Team team: league) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            // TODO: match parameters of the SQL statement and team id, abbreviation, name, conference, division, and logo
            statement_prepared.setString(1,team.getId());
            statement_prepared.setString(2,team.getAbbreviation());
            statement_prepared.setString(3,team.getName());
            statement_prepared.setString(4,team.getConference());
            statement_prepared.setString(5,team.getDivision());
            statement_prepared.setBytes(6, readLogoFile("images/mlb/logo_"+team.getAbbreviation().toLowerCase()+".jpg"));
            statement_prepared.executeUpdate();
        }
        
        db_connection.close();
    }
    /**
     * @param db_filename 
     * @param addressBook 
     * @throws java.sql.SQLException 
     */
    public void writeAddressTable(String db_filename, ArrayList<Address> addressBook) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        for (Address address: addressBook) {
            // TODO: Write an SQL statement to insert a new address into a table
            String sql = "INSERT INTO address "
                    + "(team, site, street, city, state, zip, phone, url) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            // TODO: match parameters of the SQL statement and address site, street, city, state, zip, phone, and url
            statement_prepared.setString(1, address.getTeam());
            statement_prepared.setString(2, address.getSite());
            statement_prepared.setString(3, address.getStreet());
            statement_prepared.setString(4, address.getCity());
            statement_prepared.setString(5, address.getState());
            statement_prepared.setString(6, address.getZip());
            statement_prepared.setString(7, address.getPhone());
            statement_prepared.setString(8, address.getUrl());
            statement_prepared.executeUpdate();    
        }
        
        db_connection.close();
    }
    /**
     * @param db_filename 
     * @param roster 
     * @throws java.sql.SQLException 
     */
    public void writePlayerTable(String db_filename, ArrayList<Player> roster) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        for (Player player: roster) {
            // TODO: Write an SQL statement to insert a new player into a table
            String sql = "INSERT INTO player "
                    + "(id, name, team, position)"
                    + "VALUES "
                    + "(?,?,?,?);";
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            // TODO: match parameters of the SQL statement and player id, name, position
            statement_prepared.setString(1, player.getId());
            statement_prepared.setString(2, player.getName());
            statement_prepared.setString(3, player.getTeam());
            statement_prepared.setString(4, player.getPosition());
            statement_prepared.executeUpdate();
        }
        
        db_connection.close();
    }
}