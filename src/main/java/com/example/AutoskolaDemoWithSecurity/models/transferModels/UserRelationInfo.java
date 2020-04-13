
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;


public class UserRelationInfo {
    
    private String name;
    
    private String information;
    
    private String school;
        
    private int relationID;
    
    private String role;

    public UserRelationInfo() {
    
    }
    
    public UserRelationInfo(Relationship relationship, String information) {
        this.name = relationship.getUser().getFullName();
        this.relationID = relationship.getId();    
        this.school = relationship.getDrivingSchool().getName();
        this.information = information;
        this.role = relationship.getRole().replace(",", "").substring(5);
    }
    
    public UserRelationInfo(Relationship relationship) {
        this.name = relationship.getUser().getFullName();
        this.relationID = relationship.getId();    
        this.school = relationship.getDrivingSchool().getName();
        this.information = relationship.getStatus();
        this.role = relationship.getRole().replace(",", "").substring(5);
    }

    public UserRelationInfo(ConfirmUserVerification verification) {
        this.name = verification.getUser().getFullName();
        this.information = verification.getInformation();
        this.relationID = verification.getRelationID();    
        this.school = verification.getDrivingSchool().getName();
    }
    
    public UserRelationInfo(ConfirmUserVerification verification, String information) {
        this.name = verification.getUser().getFullName();
        this.information = information;
        this.relationID = verification.getRelationID();   
        this.school = verification.getDrivingSchool().getName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getRelationID() {
        return relationID;
    }

    public void setRelationID(int relationID) {
        this.relationID = relationID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
