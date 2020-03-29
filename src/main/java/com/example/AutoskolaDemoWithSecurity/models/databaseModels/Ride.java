 
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;


import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import java.io.Serializable;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Entity
@EnableTransactionManagement
@Table(name = "rides")
public class Ride implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Ride() {
    
    }
    
    public Ride(RideDTO rideDTO) {
        this.date = rideDTO.getDate();
        this.time = rideDTO.getTime();
        this.comment = rideDTO.getComment();
        this.status = rideDTO.getStatus();
        this.vehicleID = rideDTO.getVehicleID().orElse(null);
    }

    public Ride(User student, User instructor, int vehicleID, String date, String time, String status, String comment) {
        this.instructor = instructor;
        this.vehicleID = vehicleID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public Optional<User> getStudent() {
        return Optional.ofNullable(student);
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

    public Optional<Integer> getVehicleID() {
        return Optional.ofNullable(vehicleID);
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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

}
