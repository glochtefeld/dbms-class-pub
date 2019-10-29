/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgreswriter;

import java.util.ArrayList;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author lochga01
 */
public class ReadFromCSV {
    
    public ArrayList<String> getBuildings(String filePath) {
        ArrayList<String> buildings = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allLines = reader.readAll();
            for (int i = 1; i<allLines.size();i++) {
                String[] data = allLines.get(i);
                buildings.add(data[0]);
                //System.out.println(data[0]);
            } 
       }
       catch(Exception ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE,null,ex);
        }
        return buildings;
    }
    
    public HashMap<String,String> getDepts(String filePath) {
        HashMap<String,String> departments = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allLines = reader.readAll();
            for (int i = 1; i<allLines.size();i++) {
                String[] data = allLines.get(i);
                departments.put(data[0],data[1]);
            } 
       }
       catch(Exception ex) {
            Logger.getLogger(ReadFromCSV.class.getName()).log(Level.SEVERE,null,ex);
        }
        return departments;
    }
    
    public HashMap<String,String> getMajors(String filePath) {
        HashMap<String,String> majors = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allLines = reader.readAll();
            for (int i = 1; i<allLines.size();i++) {
                String[] data = allLines.get(i);
                majors.put(data[0],data[1]);
            } 
       }
       catch(Exception ex) {
            Logger.getLogger(ReadFromCSV.class.getName()).log(Level.SEVERE,null,ex);
        }
        return majors;
    }
    
    public ArrayList<Course> getCurriculum(String filePath) {
        ArrayList<Course> curriculum = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allLines = reader.readAll();
            for (int i = 1; i<allLines.size(); i++) {
                String[] data = allLines.get(i);
                Course newCourse = new Course(
                    data[0],
                    data[1],
                    data[2]);
                curriculum.add(newCourse);
            }
        }
        catch(Exception ex) {
            Logger.getLogger(ReadFromCSV.class.getName()).log(Level.SEVERE,null,ex);
        }
        return curriculum;
    }
    
    public ArrayList<Instructor> getFaculty(String filePath) {
        ArrayList<Instructor> faculty = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> allLines = reader.readAll();
            for (int i = 1; i < allLines.size(); i++) {
                String[] data = allLines.get(i);
                boolean head = ("1".equals(data[5])) ? true : false;
                Instructor teach = new Instructor(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        head);
                faculty.add(teach);
            }
        }
        catch(Exception ex) {
            Logger.getLogger(ReadFromCSV.class.getName()).log(Level.SEVERE,null,ex);
        }
        return faculty;
    }
}
