
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.VehicleDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Entity
@EnableTransactionManagement
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true)
    private int id;
    
    @NotEmpty
    private String name;
  
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DrivingSchool.class)
    @JoinColumn(nullable = false, name = "owner_id")
    private DrivingSchool owner;
    
    private String evidenceNumber;
    
    @NotEmpty
    private String type;

    public Vehicle() {
        
    }
    
    public Vehicle(VehicleDTO vehicleDTO) {
        this.name = vehicleDTO.getName();
        this.evidenceNumber = vehicleDTO.getEvidenceNumber();
        this.type = vehicleDTO.getType();
    }

    public Vehicle(String name, DrivingSchool drivingSchool, String evidenceNumber, String type) {
        this.name = name;
        this.owner = drivingSchool;
        this.evidenceNumber = evidenceNumber;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DrivingSchool getOwner() {
        return owner;
    }

    public void setOwner(DrivingSchool owner) {
        this.owner = owner;
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
        //this.type = VehicleTypes.getVehicle(type).getName();
        this.type = type;
    }
    
}

