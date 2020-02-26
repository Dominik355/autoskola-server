
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "courses")
public class Course implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false, unique = true)
    private int id;
    
    private String category;
    
    private String allUsers;
    
    private String startDate;
    
    @ManyToOne(targetEntity = DrivingSchool.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "school_ID")
    private DrivingSchool drivingSchool;
    
    @NotEmpty
    private int capacity;
    
    private float price;

    public Course() {
        
    }

    public Course(String category, String startDate, DrivingSchool drivingSchool, int capacity, float price) {
        this.category = category;
        this.startDate = startDate;
        this.drivingSchool = drivingSchool;
        this.capacity = capacity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addUser(int userID) {
        if(genNumberOfUsers()<capacity) {
            this.allUsers +=userID+",";
        }
        else {
            //Course is full ... alebo to bude v service
        }
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public DrivingSchool getDrivingSchool() {
        return drivingSchool;
    }

    public void setDrivingSchool(DrivingSchool drivingSchool) {
        this.drivingSchool = drivingSchool;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
    public List<Integer> getAllUsers() {
        List<Integer> list = new ArrayList<>();
        int val = -1;
                
        for(String f : allUsers.split(",")) {
            try{
                val = Integer.parseInt(f);
            } catch(NumberFormatException e) {
                
            }
            list.add(val);
        }
        return list;
    }
    
    public boolean containsUser(int userID) {
        List<Integer> users = getAllUsers();
        if (users.stream().anyMatch(s -> (s == userID))) {
            return true;
        }
        return false;
    }
    
    public int genNumberOfUsers() {
        return allUsers.split(",").length;
    }
    
}
