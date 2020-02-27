
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;


import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.UniqueSchoolName;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Entity
@Table(name = "driving_schools")
@EnableTransactionManagement
public class DrivingSchool implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true)
    private int id;
    
    @NotEmpty
    @UniqueSchoolName
    private String name;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "owner_id")
    private User owner;
    
    @NotEmpty
    private String phoneNumber;
    
    @NotEmpty
    private String email;
    
    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    
    @NotEmpty
    private String address;

    public DrivingSchool() {
        
    }
    
    public DrivingSchool(DrivingSchoolDTO schoolDTO) {
        this.name = schoolDTO.getName();
        this.address = schoolDTO.getAddress();
        this.email = schoolDTO.getEmail();
        this.phoneNumber = schoolDTO.getPhoneNumber();
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public DrivingSchool(String name, User owner, String phoneNumber, String email, Date creationDate, String address) {
        this.name = name;
        this.owner = owner;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
   
}
