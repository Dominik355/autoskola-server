
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
        this.name = relationship.getUser().getSurname() + " " + relationship.getUser().getName();
        this.relationID = relationship.getId();    
        this.school = relationship.getDrivingSchool().getName();
        this.information = information;
        this.role = relationship.getRole().replace(",", "").substring(5);
    }
    
    public UserRelationInfo(Relationship relationship) {
        this.name = relationship.getUser().getSurname() + " " + relationship.getUser().getName();
        this.relationID = relationship.getId();    
        this.school = relationship.getDrivingSchool().getName();
        this.information = relationship.getStatus();
        this.role = relationship.getRole().replace(",", "").substring(5);
    }

    public UserRelationInfo(ConfirmUserVerification verification) {
        String[] arr = verification.getInformation().split("%");
        this.name = verification.getUser().getSurname() + " " + verification.getUser().getName();
        this.information = verification.getInformation();
        this.relationID = verification.getRelationID();    
        this.school = verification.getDrivingSchool().getName();
        this.role = verification.getInformation().substring(4, verification.getInformation().indexOf(" ", 6));
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
