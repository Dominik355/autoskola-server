
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;
import java.util.List;


public class AuthenticationResponse implements Serializable {
    
    private String jwtToken;
    private String emitterID;
    private String NotificationURL;
    private int schoolCount;
    private List<UserRelationInfo> relations;

    public AuthenticationResponse() {
   
    }

    public AuthenticationResponse(String jwtToken, String emitterID, String NotificationURL, int schoolCount, List<UserRelationInfo> relations) {
        this.jwtToken = jwtToken;
        this.emitterID = emitterID;
        this.NotificationURL = NotificationURL;
        this.schoolCount = relations.size();
        this.relations = relations;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getEmitterID() {
        return emitterID;
    }

    public void setEmitterID(String emitterID) {
        this.emitterID = emitterID;
    }

    public String getNotificationURL() {
        return NotificationURL;
    }

    public void setNotificationURL(String NotificationURL) {
        this.NotificationURL = NotificationURL;
    }

    public int getSchoolCount() {
        return schoolCount;
    }

    public void setSchoolCount(int schoolCount) {
        this.schoolCount = schoolCount;
    }

    public List<UserRelationInfo> getRelations() {
        return relations;
    }

    public void setRelations(List<UserRelationInfo> relations) {
        this.relations = relations;
    }
     
}