
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.services.RideService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/instructor")
@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
public class InstructorController {
    
    @Autowired
    private RideService rideService;
    
    @GetMapping(value = {"/instructor"})
    public String helloInstructor() {
        return "Hello instructor";
    }
    
    @PostMapping(value = "/addRide")
    public ResponseEntity addRide (@RequestBody RideDTO ride, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.addRide(ride, request.getIntHeader("Relation")));
    }
    
    @DeleteMapping(value = "/removeRide/{id}")
    public ResponseEntity removeRide(@PathVariable int id, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.removeRide(id, request.getIntHeader("Relation")));
    }
    
}
