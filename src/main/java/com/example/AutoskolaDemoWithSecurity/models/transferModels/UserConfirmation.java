
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;


public class UserConfirmation {
    
    private String name;
    
    private String information;
    
    private String school;
        
    private int relationID;

    public UserConfirmation() {
    
    }

    public UserConfirmation(ConfirmUserVerification verification) {
        this.name = verification.getUser().getFullName();
        this.information = verification.getInformation();
        this.relationID = verification.getRelationID();    
        this.school = verification.getDrivingSchool().getName();
    }
    
    public UserConfirmation(ConfirmUserVerification verification, String information) {
        this.name = verification.getUser().getFullName();
        this.information = information;
        this.relationID = verification.getRelationID();   
        this.school = verification.getDrivingSchool().getName();
    }

    public UserConfirmation(String name, String information, String school, int relationID) {
        this.name = name;
        this.information = information;
        this.school = school;
        this.relationID = relationID;
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
    
}
