
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;
import java.util.List;


public class InstructorRides implements Serializable{
    
    String instructorName;
    
    String date;
    
    List<RideIdTime> rides;

    public InstructorRides() {
    
    }

    public InstructorRides(String instructorName, List<RideIdTime> rides, String date) {
        this.instructorName = instructorName;
        this.rides = rides;
        this.date = date;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public List<RideIdTime> getRides() {
        return rides;
    }

    public void setRides(List<RideIdTime> rides) {
        this.rides = rides;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
