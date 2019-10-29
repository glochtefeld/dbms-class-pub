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
public class Course {
    private String number;
    private String name;
    private String department;
    
    public Course(String num, String name, String dept) {
        this.number = num;
        this.name = name;
        this.department = dept;
    }
    
    public String getNumber() {
        return this.number;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDept() {
        return this.department;
    }
    
}
