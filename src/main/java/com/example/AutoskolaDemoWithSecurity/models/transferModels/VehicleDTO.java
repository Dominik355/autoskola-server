
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Vehicle;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;


public class VehicleDTO implements Serializable {
    
    int id;
    
    @NotEmpty
    private String name;
    
    private String evidenceNumber;
    
    @NotEmpty
    private String type;

    public VehicleDTO() {
    
    }

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.name = vehicle.getName();
        this.type = vehicle.getType();
        this.evidenceNumber = vehicle.getEvidenceNumber();
    }

    public VehicleDTO(String name, String evidenceNumber, String type) {
        this.name = name;
        this.evidenceNumber = evidenceNumber;
        this.type = type;
    }
    
    public VehicleDTO(String name, String evidenceNumber, String type, int id) {
        this.name = name;
        this.evidenceNumber = evidenceNumber;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvidenceNumber() {
        return evidenceNumber;
    }

    public void setEvidenceNumber(String evidenceNumber) {
        this.evidenceNumber = evidenceNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
