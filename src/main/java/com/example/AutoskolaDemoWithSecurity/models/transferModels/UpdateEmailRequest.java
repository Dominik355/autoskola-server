
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;


public class UpdateEmailRequest {
    
    @EmailValidConstraint
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
