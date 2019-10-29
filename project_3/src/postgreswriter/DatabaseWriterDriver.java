package postgreswriter;
/**
 * @author Gavin Lochtefeld
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseWriterDriver {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseWriter dw = new DatabaseWriter();
        String db_filename = "newtest";
        if (args.length != 0) {
            db_filename = args[0];
        }
        try {
            //Create database and drop any old db
            dw.createDatabase(db_filename);
            // Create tables
            dw.createTables(db_filename);
            //dw.addViews(db_filename);
            //populate tables
            dw.addBuildings(db_filename,"data/buildings.csv");
            dw.addDepartments(db_filename, "data/departments.csv");
            dw.addMajors(db_filename, "data/majors.csv");
            dw.addFaculty(db_filename, "data/faculty.csv");
            dw.addCourses(db_filename, "data/Website/curriculum.csv");
            dw.addSemesters(db_filename);
            dw.addSections(db_filename);
            dw.addStudents(db_filename);
            dw.addEnrollment(db_filename);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseWriterDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Finished successfully.");
    }
    
}
