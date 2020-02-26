
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Vehicle;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import static org.hibernate.criterion.Projections.id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class VehicleService {
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
   /* 
    //len pre OWNER 
    public ResponseEntity addVehicle(Vehicle vehicle) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        DrivingSchool school = schoolRepository.findById(id).orElseThrow();
    }
    
    public ResponseEntity removeVehicle(int vehicleID) {
        
    }
    
    public ResponseEntity modifyVehicle(Vehicle vehicle) {
        
    }
        
    private boolean isItOwner(int id) {
        // zo security contextu vyberie id uzivatela , porovna s id uzivatela z relationshipu, ak sa zhoduju, a role je owner, co sa skontroluje v 
        //controller metode, tak prida do autoskoly z relationshipu vozidlo, cize vrati tuto true
    }
    */
}
