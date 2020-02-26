
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class InstructorRides implements Serializable{
    
    String instructorName;
    
    String[] times;

    public InstructorRides() {
    
    }

    public InstructorRides(String instructorName, String[] times) {
        this.instructorName = instructorName;
        this.times = times;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }

}
