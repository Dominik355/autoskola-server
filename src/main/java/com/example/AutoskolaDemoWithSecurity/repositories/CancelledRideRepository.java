
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CancelledRide;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CancelledRideRepository extends JpaRepository<CancelledRide, Integer> {
    
    
    
}
