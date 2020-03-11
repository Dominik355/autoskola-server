
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class AuthenticationResponse implements Serializable {
    
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private int relationID;
    private UserProfileInfo info;

    public AuthenticationResponse(String jwtToken, int relationID, UserProfileInfo info) {
        this.jwtToken = jwtToken;
        this.relationID = relationID;
        this.info = info;
    }

    public String getJwtToken() {
        return this.jwtToken; 
    }

    public int getRelationIDd() {
        return relationID;
    }

    public UserProfileInfo getInfo() {
        return info;
    }

    public void setRelationID(int id) {
        this.relationID= id;
    }

    public void setInfo(UserProfileInfo info) {
        this.info = info;
    }
  
}