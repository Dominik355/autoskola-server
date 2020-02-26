
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompletedRideService {
    
    @Autowired
    private CompletedRideRepository crr;
    
    public ResponseEntity getCompletedRides() {
        return null;
        
    }
    
}
