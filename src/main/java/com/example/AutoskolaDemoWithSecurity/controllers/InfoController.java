
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.VehicleRepository;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
@RequestMapping("/info")
public class InfoController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
    
    @Autowired
    private RelationshipRepository rr;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private CompletedRideRepository crr;
    
    
    @GetMapping(value = "/schoolsAvailable")
    @ApiOperation(value = "zoznam skol - len informacne")
    public ResponseEntity getDrivingSchools() {
        return ResponseEntity.ok(schoolService.getDrivingSchools());
    }
    
    @GetMapping(value = "/linksAvailable")
    @ApiOperation(value = "zoznam linkov - len informacne")
    public ResponseEntity getAvailableLinks() {
        Object[] mappedUrls = requestMappingHandlerMapping.getHandlerMethods().keySet().toArray();
        return ResponseEntity.ok(mappedUrls);
    }
    
    @ApiOperation(value = "zoznam uzivatelov - len informacne")
    @GetMapping(value = "/listOfUsers")
    public ResponseEntity getListOfUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    @GetMapping(value = {"/allRelations"})
    public ResponseEntity getAlRelations() {
        return ResponseEntity.ok(rr.findAll());
    }

    @GetMapping(value = {"/allSchools"})
    public ResponseEntity getAllSchools() {
        return ResponseEntity.ok(schoolRepository.findAll());
    }
    
    @GetMapping(value = {"/allVehicles"})
    public ResponseEntity getAllVehicles() {
        return ResponseEntity.ok(vehicleRepository.findAll());
    }
    
    @GetMapping(value = {"/allRides"})
    public ResponseEntity getAllRides() {
        return ResponseEntity.ok(rideRepository.findAll());
    }
    
    @GetMapping(value = {"/allCompletedRides"})
    public ResponseEntity getAllCompletedRides() {
        return ResponseEntity.ok(crr.findAll());
    }
    
}
