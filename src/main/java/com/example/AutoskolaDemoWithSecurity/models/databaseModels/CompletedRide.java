
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
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
@Table(name = "completed_rides")
public class CompletedRide implements Serializable{
    
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
    
    @ManyToOne(targetEntity = Relationship.class)
    @JoinColumn(name = "relationship_ID")
    private Relationship relationship;
    
    //toto nebude foreign key, lebo sa moze stat, ze Vozidlo sa odstrani, ale jazdu odstranit nechcem
    @Column(name = "vehicle_ID")
    private int vehicleID;
    
    @NotEmpty
    private String date;
    
    @NotEmpty
    private String time;
    
    private String comment;

    public CompletedRide() {
    
    }

    public CompletedRide(User student, User instructor, int vehicleID, String date, String time, String comment) {
        this.instructor = instructor;
        this.vehicleID = vehicleID;
        this.date = date;
        this.time = time;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
    
}
