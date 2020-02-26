
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class AuthenticationResponse implements Serializable {
    
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private int id;
    private UserProfileInfo info;

    public AuthenticationResponse(String jwtToken, int id, UserProfileInfo info) {
        this.jwtToken = jwtToken;
        this.id = id;
        this.info = info;
    }

    public String getJwtToken() {
        return this.jwtToken; 
    }

    public int getId() {
        return id;
    }

    public UserProfileInfo getInfo() {
        return info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInfo(UserProfileInfo info) {
        this.info = info;
    }
  
}