
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Entity
@EnableTransactionManagement
@Table(name = "user_confirmations")
public class ConfirmUserVerification implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true, nullable = false)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private DrivingSchool drivingSchool;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    //informacia ci ide o instruktora, alebo ziaka+ pripadne do ktoreho kurzu sa hlasi
    private String information;
        
    @Column(name = "relation_id")
    private int relation;

    public ConfirmUserVerification() {
    
    }

    public ConfirmUserVerification(DrivingSchool drivingSchool, User user, String information, int relationID) {
        this.drivingSchool = drivingSchool;
        this.user = user;
        this.information = information;
        this.relation = relationID;
    }

    public int getId() {
        return id;
    }

    public DrivingSchool getDrivingSchool() {
        return drivingSchool;
    }

    public void setDrivingSchool(DrivingSchool drivingSchool) {
        this.drivingSchool = drivingSchool;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getRelationID() {
        return relation;
    }

    public void setRelationID(int relationID) {
        this.relation = relationID;
    }

}
