
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Entity
@EnableTransactionManagement
@Table(name = "cancelled_rides")
public class CancelledRide implements Serializable{
    
    @Id
    @Column(updatable = false, nullable = false, insertable = false, unique = true)
    private int id;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_ID")
    private User student;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_ID")
    private User instructor;
    
    @ManyToOne(targetEntity = DrivingSchool.class)
    @JoinColumn(name = "driving_school_ID")
    private DrivingSchool drivingSchool;
    
    //toto nebude foreign key, lebo sa moze stat, ze Vozidlo sa odstrani, ale jazdu odstranit nechcem
    @Column(name = "vehicle_ID")
    private int vehicleID;
    
    @NotEmpty
    private String date;
    
    @NotEmpty
    private String time;
    
    private String status;
    
    private String comment;

    public CancelledRide() {
    
    }
    
    public CancelledRide(Ride ride) {
        this.id = ride.getId();
        this.student = ride.getStudent().orElse(null);
        this.drivingSchool = ride.getDrivingSchool();
        this.instructor = ride.getInstructor();
        this.vehicleID = ride.getVehicleID().orElse(null);
        this.date = ride.getDate();
        this.time = ride.getTime();
        this.status = "CANCELLED";
    }

    public CancelledRide(User student, User instructor, int vehicleID, String date, String time, String comment) {
        this.instructor = instructor;
        this.vehicleID = vehicleID;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.status = "CANCELLED";
    }

    public int getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
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

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDrivingSchool(DrivingSchool drivingSchool) {
        this.drivingSchool = drivingSchool;
    }

    public DrivingSchool getDrivingSchool() {
        return drivingSchool;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
