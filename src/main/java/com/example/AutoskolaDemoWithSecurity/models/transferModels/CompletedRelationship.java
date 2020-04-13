
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class CompletedRelationship {
    
    private Timestamp startDate;
    
    private Timestamp endDate;
    
    private List<RideDTO> rides;

    public CompletedRelationship() {
    
    }

    public CompletedRelationship(Timestamp startDate, Timestamp endDate, List<RideDTO> rides) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rides = rides;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public List<RideDTO> getRides() {
        return rides;
    }

    public void setRides(List<RideDTO> rides) {
        this.rides = rides;
    }
    
    public void addRide(RideDTO ride) {
        if(this.rides.isEmpty()) {
            rides = new ArrayList<>();
        }
        rides.add(ride);
    }
    
}
