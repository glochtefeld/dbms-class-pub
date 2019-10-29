package postgreswriter;
/**
 * @author Gavin Lochtefeld
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import java.sql.ResultSet;
import java.util.Random;

public class DatabaseWriter {
    private final String POSTGRESDBPATH = "jdbc:postgresql://localhost:5432/";
    private ReadFromCSV reader = new ReadFromCSV();
       
    public void createDatabase(String name) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + "postgres",
                            "postgres",
                            "project3");
            Statement statement = c.createStatement();
            statement.executeUpdate("DROP DATABASE IF EXISTS " + name + ";");
            statement.executeUpdate("CREATE DATABASE " + name + ";");
            c.close();
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void createTables(String db_filename) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres", 
                            "project3");
            Statement statement = c.createStatement();
            
            statement.executeUpdate("DROP TABLE IF EXISTS DEPARTMENT;");
            statement.executeUpdate(
                "CREATE TABLE DEPARTMENT ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name TEXT NOT NULL,"
                    + "building INTEGER NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS FACULTY;");
            statement.executeUpdate(
                "CREATE TABLE FACULTY ("
                        + "id SERIAL PRIMARY KEY,"
                        + "department INTEGER NOT NULL,"
                        + "name TEXT NOT NULL,"
                        + "office TEXT,"
                        + "extension TEXT,"
                        + "email TEXT NOT NULL,"
                        + "isHead BOOLEAN NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS COURSE;");
            statement.executeUpdate(
                "CREATE TABLE COURSE ("
                        + "id SERIAL PRIMARY KEY,"
                        + "title TEXT NOT NULL,"
                        + "number INTEGER NOT NULL,"
                        + "department INTEGER NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS LOCATION");
            statement.executeUpdate(
                "CREATE TABLE LOCATION ("
                        + "id SERIAL PRIMARY KEY,"
                        + "name TEXT NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS SEMESTER;");
            statement.executeUpdate(
                "CREATE TABLE SEMESTER ("
                        + "id SERIAL PRIMARY KEY,"
                        + "year TEXT NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS MAJOR");
            statement.executeUpdate(
                "CREATE TABLE MAJOR ("
                        + "id SERIAL PRIMARY KEY,"
                        + "name TEXT NOT NULL,"
                        + "department INTEGER NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS SECTION");
            statement.executeUpdate(
                "CREATE TABLE SECTION ("
                        + "id SERIAL PRIMARY KEY,"
                        + "course INTEGER NOT NULL,"
                        + "semester INTEGER NOT NULL,"
                        + "professor INTEGER NOT NULL);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS STUDENT");
            statement.executeUpdate(
                "CREATE TABLE STUDENT ("
                        + "id SERIAL PRIMARY KEY,"
                        + "name TEXT NOT NULL,"
                        + "major INTEGER);");
            
            statement.executeUpdate("DROP TABLE IF EXISTS ENROLLMENT");
            statement.executeUpdate(
                "CREATE TABLE ENROLLMENT ("
                        + "enrollrecord SERIAL PRIMARY KEY,"
                        + "student INTEGER NOT NULL,"
                        + "section INTEGER NOT NULL);");
            
            c.close();
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addBuildings(String db_filename, String csv) throws SQLException {
        Connection c = null;
        try {
            ArrayList<String> buildings = reader.getBuildings(csv);
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres",
                            "project3");
            Statement statement = c.createStatement();
            // SQL goes here
            
            for (String name : buildings) {
                //System.out.println("Adding " + name + " to buildings table");
                statement.executeUpdate(
                    "INSERT INTO LOCATION "
                            + "(name) "
                            + "VALUES ('" + name + "');");
            }
            c.close();
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addDepartments(String db_filename, String csv) throws SQLException {
        Connection c = null;
        try {
            HashMap<String,String> departments = reader.getDepts(csv);
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres",
                            "project3");
            Statement statement = c.createStatement();
            for (String name : departments.keySet()) {
                //System.out.println("Adding " + name + ": " + departments.get(name));
                statement.executeUpdate(
                    "INSERT INTO DEPARTMENT "
                            + "(name, building) "
                            + "VALUES ('" + name + "'," + departments.get(name) + ");");
            }
            c.close();
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addMajors(String db_filename, String csv) throws SQLException {
        Connection c = null;
        try {
            HashMap<String,String> majors = reader.getMajors(csv);
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename, "postgres","project3");
            Statement statement = c.createStatement();
            for (String name : majors.keySet() ) {
                statement.executeUpdate(
                        "INSERT INTO MAJOR "
                                + "(name, department) "
                                + "VALUES "
                                + "('" + name + "'," + majors.get(name) + ");");
            }
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addFaculty(String db_filename, String csv) throws SQLException {
        Connection c = null;
        try {
            ArrayList<Instructor> faculty = reader.getFaculty(csv);
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename, "postgres","project3");
            Statement statement = c.createStatement();
            for (Instructor teach : faculty ) {
                statement.executeUpdate(
                        "INSERT INTO FACULTY "
                                + "(department,name,office,extension,email,ishead) "
                                + "VALUES "
                                + "('" + teach.getDepartment() + "','"
                                + teach.getName() + "','"
                                + teach.getOffice() + "','"
                                + teach.getExtension() + "','"
                                + teach.getEmail() + "',"
                                + teach.isHead() + ");");
            }
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addCourses(String db_filename, String csv) throws SQLException {
        Connection c = null;
        try {
            ArrayList<Course> curr = reader.getCurriculum(csv);
            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename, "postgres","project3");
            Statement statement = c.createStatement();
            for (Course learningOppurtunity : curr ) {
                //System.out.println("Inserted " + learningOppurtunity.getName());
                statement.executeUpdate(
                        "INSERT INTO COURSE "
                                + "(title, number, department) "
                                + "VALUES "
                                + "('" + learningOppurtunity.getName() + "','"
                                + learningOppurtunity.getNumber() + "',"
                                + learningOppurtunity.getDept() + ");");
            }
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addSemesters(String db_filename) {
        Connection c = null;
        try {            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename, "postgres","project3");
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO SEMESTER (year) VALUES ('2016-2017');");
            statement.executeUpdate("INSERT INTO SEMESTER (year) VALUES ('2017-2018');");
            statement.executeUpdate("INSERT INTO SEMESTER (year) VALUES ('2018-2019');");
            statement.executeUpdate("INSERT INTO SEMESTER (year) VALUES ('2019-2020');");   
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addSections(String db_filename) throws SQLException {
        Connection c = null;
        try {            
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename, "postgres","project3");
            Statement statement = c.createStatement();
            ResultSet course100ID = statement.executeQuery(
                "SELECT id FROM COURSE WHERE number < 200;");
            
            int romanID = 37;
            
            while (course100ID.next()) {
                int courseID = course100ID.getInt("id");
                for (int year = 1; year<5;year++) {
                    for (int i = 0; i<4;i++) {
                Statement nuStatement = c.createStatement();
                nuStatement.executeUpdate("INSERT INTO SECTION "
                        + "(course,semester,professor) "
                        + "VALUES ('"
                        + courseID + "','"
                        + year + "','"
                        + romanID + "');");
                }
                }
            }
            
            Statement res2 = c.createStatement();
            ResultSet course200ID = res2.executeQuery(
                "SELECT id FROM COURSE WHERE number >=200 AND number <300;");
            
            while (course200ID.next()) {
                int courseID = course200ID.getInt("id");
                for (int year = 1; year<5;year++) {
                    for (int i = 0; i<3;i++) {
                Statement nuStatement = c.createStatement();
                nuStatement.executeUpdate("INSERT INTO SECTION "
                        + "(course,semester,professor) "
                        + "VALUES ('"
                        + courseID + "','"
                        + year + "','"
                        + romanID + "');");
                    }
                }
            }
            Statement res3 = c.createStatement();
            ResultSet course300ID = statement.executeQuery(
                "SELECT id FROM COURSE WHERE number >=300 AND number <400;");
            while (course300ID.next()) {
                int courseID = course300ID.getInt("id");
                for (int year = 1; year<5;year++) {
                    for (int i = 0; i<2;i++) {
                Statement nuStatement = c.createStatement();
                nuStatement.executeUpdate("INSERT INTO SECTION "
                        + "(course,semester,professor) "
                        + "VALUES ('"
                        + courseID + "','"
                        + year + "','"
                        + romanID + "');");
                    }
                }
            }
            
            Statement res4 = c.createStatement();
            ResultSet course400ID = statement.executeQuery(
                "SELECT id FROM COURSE WHERE number >=400;");
            
            while (course400ID.next()) {
                int courseID = course400ID.getInt("id");
                for (int year = 1; year<5;year++) {
                Statement nuStatement = c.createStatement();
                nuStatement.executeUpdate("INSERT INTO SECTION "
                        + "(course,semester,professor) "
                        + "VALUES ('"
                        + courseID + "','"
                        + year + "','"
                        + romanID + "');");
                }
            }   
        }
        catch (Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void addStudents(String db_filename) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres", 
                            "project3");
            Statement statement = c.createStatement();
            for (int i = 0; i<3000;i++) {
                Faker fake = new Faker();
                String name = fake.name().fullName();
                name = name.replaceAll("\'","");
                
                //System.out.println("Added " + name);
                
                statement.executeUpdate(
                        "INSERT INTO STUDENT "
                                + "(name,major)"
                                + "VALUES ('" + name + "',null);");
            }
            
        }
        catch(Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);

        }
    }
    
    public void addEnrollment(String db_filename) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres", 
                            "project3");
            Statement getSectionNum = c.createStatement();
            ResultSet maxSectionNum = getSectionNum.executeQuery("SELECT COUNT(*) as cnt FROM SECTION;");
            int sections = 0;
            while (maxSectionNum.next()) {
                sections = maxSectionNum.getInt("cnt");
            }
            
            Statement getStudNum = c.createStatement();
            ResultSet maxStudNum = getStudNum.executeQuery("SELECT COUNT(*) as cnt FROM SECTION;");
            int students = 0;
            while (maxStudNum.next()) {
                students = maxStudNum.getInt("cnt");
            }
            
            Random sChooser = new Random();
            Statement addStudent = c.createStatement();
            for (int i=0; i<students;i++) {
                for (int j = 0; j<10;j++) {
                    int section = sChooser.nextInt(sections) + 1;
                    addStudent.executeUpdate(
                        "INSERT INTO ENROLLMENT (student, section)"
                                + "VALUES"
                                + "('" + i + "','"
                                + section + "')");
                }                
            }
            
        }
        catch(Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);

        }
    }
    
    public void addTriggers(String db_filename) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres", 
                            "project3");
            Statement statement = c.createStatement();
        }
        catch(Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);

        }
    }
    
    public void addViews(String db_filename) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(POSTGRESDBPATH + db_filename,
                            "postgres", 
                            "project3");
            Statement statement = c.createStatement();
            statement.execute("CREATE VIEW students_in_section AS "
                    + "SELECT SECTION.id, STUDENT.name,COURSE.name "
                    + "FROM ENROLLMENT JOIN SECTION ON ENROLLMENT.SECTION = SECTION.ID "
                    + "JOIN STUDENT ON STUDENT.ID = ENROLLMENT.STUDENT"
                    + "JOIN COURSE ON COURSE.ID = SECTION.COURSE "
                    + "GROUP BY SECTION.ID "
                    + "HAVING COUNT(*) > 0;");
        }
        catch(Exception ex) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE,null,ex);

        }
    }
    
    
    
}

/*
if (level.matches(1\d+)) { //make 4 }
else if (level.matches(2\d+)) { //make 3  }
else if (level.matches(3\d+)) { //make 2 }
elise if (level.matches(4\d+)) { //make 1 }

*/