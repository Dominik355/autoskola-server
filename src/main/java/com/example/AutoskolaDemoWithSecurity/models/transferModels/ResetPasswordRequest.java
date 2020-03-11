
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import java.io.Serializable;


public class ResetPasswordRequest implements Serializable {
        
    @EmailValidConstraint
    private String userEmail;

    public ResetPasswordRequest() { }

    public ResetPasswordRequest(String userEmail) {
        this.userEmail = userEmail; 
    }

    public String getUserEmail() {
        return this.userEmail; 
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail; 
    }

}