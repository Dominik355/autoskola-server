
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PhoneNumberConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.UniqueSchoolName;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;


public class DrivingSchoolDTO implements Serializable {
    
    private int id;
    
    @NotEmpty
    @UniqueSchoolName
    private String name;
    
    @NotEmpty
    @PhoneNumberConstraint
    private String phoneNumber;
    
    @NotEmpty
    @EmailValidConstraint
    private String email;
    
    @NotEmpty
    private String address;
    
    @NotEmpty
    @EmailValidConstraint
    private String ownerEmail;

    public DrivingSchoolDTO() {

    }
    
    public DrivingSchoolDTO(DrivingSchool school) {
        this.id = school.getId();
        this.address = school.getAddress();
        this.email = school.getEmail();
        this.name = school.getName();
        this.phoneNumber = school.getPhoneNumber();
    }

    public DrivingSchoolDTO(String name, String phoneNumber, String email, String address, String ownerEmail) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.ownerEmail = ownerEmail;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

}
