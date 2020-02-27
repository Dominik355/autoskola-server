
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PhoneNumberConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.UniqueSchoolName;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;


public class DrivingSchoolDTO implements Serializable {

    
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

    public DrivingSchoolDTO() {

    }

    public DrivingSchoolDTO(String name, String phoneNumber, String email, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
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
    

}
