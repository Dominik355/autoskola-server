
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import java.io.Serializable;


public class UpdateEmailRequest implements Serializable{
    
    @EmailValidConstraint(message = "{userDTO.emailValid}")
    private String newEmail;

    public UpdateEmailRequest() {}

    public UpdateEmailRequest(String newEmail) {
        this.newEmail = newEmail; 
    }

    public String getNewEmail() {
        return this.newEmail; 
    }

    public void setNewEmail(String newEmail) {
            this.newEmail = newEmail; 
    }
}
