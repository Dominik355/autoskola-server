
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
@Table(name = "relationships")
public class Relationship implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false, unique = true)
    private int id;
    
    @ManyToOne(targetEntity = DrivingSchool.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "driving_School")
    private DrivingSchool drivingSchool;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;
    
    private Timestamp endingdate;
    
    @NotEmpty
    @Column(name = "position")
    private String role;
    
    @Column(columnDefinition = "varchar(255) default ''")
    private String status;
    
    private boolean activate;

    public Relationship() {
        
    }

    public Relationship(DrivingSchool drivingSchool, User user, boolean activate) {
        this.drivingSchool = drivingSchool;
        this.user = user;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.role = user.getRoles();
        this.activate = activate;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Timestamp getEndingdate() {
        return endingdate;
    }

    public void setEndingdate(Timestamp endingdate) {
        this.endingdate = endingdate;
    }

    public String getRole() {
        return role;
    }
    
    public List<String> getRoles() {
        return Arrays.stream(user.getRoles().split(",")).collect(Collectors.toList());
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        if(status == null) return "";
        else return status;
    }
    

    @Override
    public String toString() {
        return "role: "+this.role+
                ", active: "+this.activate+
                        ", drivingSchoold: "+this.drivingSchool+
                                ", endingDate: "+this.endingdate+
                                        ", id: "+this.id+
                                                ", role: "+this.role+
                                                        ", status: "+this.status+
                                                                ", user: "+this.user;
    }
    
}
