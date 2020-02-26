
package com.example.AutoskolaDemoWithSecurity.models.transferModels;


public class ResetPasswordRequest {
        
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