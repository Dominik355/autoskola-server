
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class AuthenticationResponse implements Serializable {
    
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private int relationID;
    private String emitterID;
    private UserProfileInfo info;
    private String NotificationURL;
   

    public AuthenticationResponse(String jwtToken, int relationID, UserProfileInfo info, String emitterID, String NotificationURL) {
        this.jwtToken = jwtToken;
        this.relationID = relationID;
        this.info = info;
        this.emitterID = emitterID;
        this.NotificationURL = NotificationURL;
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

    public String getEmitterID() {
        return emitterID;
    }

    public void setEmitterID(String emitterID) {
        this.emitterID = emitterID;
    }

    public void setNotificationURL(String NotificationURL) {
        this.NotificationURL = NotificationURL;
    }

    public String getNotificationURL() {
        return NotificationURL;
    }
  
}