
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Vehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Size;

// pri ziakovi - poslat len meno instruktora
@ApiModel(description = "Pri vytvarani jazdy staci zadam date a time, pripadne vehicleID a komentar")
public class RideDTO implements Serializable {
    
    private int id;
    
    @Size(max = 10)
    @ApiModelProperty(notes = "date in format 'yyyy-mm-dd'")
    private String date;
    
    @Size(max = 5)
    @ApiModelProperty(notes = "Time in format 'hh:mm'")
    private String time;
    
    private String status;

    @Size(max = 255)
    private String comment;

    private int vehicleID;
    
    private Vehicle vehicle;
    
    public RideDTO() {
        
    }
    
    public RideDTO(CompletedRide ride) {
        this.comment = ride.getComment();
        this.date = ride.getDate();
        this.id = ride.getId();
        this.time = ride.getTime();
    }

    public RideDTO(String date, String time, String status, String comment, int vehicleID) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Time: "+getTime()+", date: "+getDate()+", status: "+getStatus();
    }
    
}
