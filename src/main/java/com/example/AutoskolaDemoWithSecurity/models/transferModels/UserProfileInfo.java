
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.io.Serializable;
import java.sql.Timestamp;


public class UserProfileInfo implements Serializable{
    
    private int id;
    
    private String fullName;
    
    private String email;
    
    private String phoneNumber;
    
    private Timestamp startDate;
    
    private int ridesCompleted;

    public UserProfileInfo() {
        
    }

    public UserProfileInfo(int id, String fullName, String email, String phoneNumber, Timestamp startDate, int ridesCompleted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.ridesCompleted = ridesCompleted;
    }

    public UserProfileInfo(String fullName, String email, String phoneNumber, Timestamp startDate, int ridesCompleted) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.ridesCompleted = ridesCompleted;
    }
    
    public UserProfileInfo(User user, int completedRides) {
        this.id = user.getId();
        this.fullName = user.getName()+" "+user.getSurname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.startDate = user.getCreatedOn();
        this.ridesCompleted = completedRides;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public int getRidesCompleted() {
        return ridesCompleted;
    }

    public void setRidesCompleted(int ridesCompleted) {
        this.ridesCompleted = ridesCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
