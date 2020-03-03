
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Vehicle;
import java.io.Serializable;
import javax.validation.constraints.Size;

// pri ziakovi - poslat len meno instruktora
public class RideDTO implements Serializable {
    
    @Size(max = 10)
    private String date;
    
    @Size(max = 5)
    private String time;
    
    private boolean status;

    @Size(max = 255)
    private String comment;

    private int vehicleID;
    
    private User student;
    private User instructor;
    private Vehicle vehicle;
    private int id;
    
    public RideDTO() {
        
    }
    
    public RideDTO(CompletedRide ride) {
        this.comment = ride.getComment();
        this.date = ride.getDate();
        this.id = ride.getId();
        this.student = ride.getStudent();
        this.time = ride.getTime();
    }

    public RideDTO(String date, String time, boolean status, String comment, int vehicleID) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.comment = comment;
        this.vehicleID = vehicleID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getInstructor() {
        return instructor;
    }

    public User getStudent() {
        return student;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
}
