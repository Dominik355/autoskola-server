
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.PasswordStrengthConstraint;


public class UpdatePasswordRequest {
    private String oldPassword;
    
    @PasswordStrengthConstraint
    private String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordRequest() {}

    public String getOldPassword() {
        return this.oldPassword; 
    }

    public String getNewPassword() {
        return this.newPassword; 
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword; 
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword; 
    }
}
