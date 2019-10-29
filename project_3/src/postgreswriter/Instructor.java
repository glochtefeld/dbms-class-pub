/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgreswriter;

/**
 *
 * @author lochga01
 */
public class Instructor {
    private String department, name, office, extension, email;
    private boolean head;
    
    public Instructor(String dept, 
            String name, 
            String office, 
            String extension, 
            String email,
            boolean head) {
        this.department = dept;
        this.name = name;
        this.office = office;
        this.extension = extension;
        this.email = email;
        this.head = head;
    }
    
    public String getDepartment() {
        return this.department;
    }
    public String getName() {
        return this.name;
    }
    public String getOffice() {
        return this.office;
    }
    public String getExtension() {
        return this.extension;
    }
    public String getEmail() {
        return this.email;
    }
    public boolean isHead() {
        return this.head;
    }
    
}
