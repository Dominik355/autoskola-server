
package com.example.AutoskolaDemoWithSecurity.controllers;


import com.example.AutoskolaDemoWithSecurity.services.RideService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentController {
        
    Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    @Autowired
    private  RideService rideService;
    
    @GetMapping(value = {"/hello"})
    public String helloStudent() {
        return "Hello student";
    }
    
    @GetMapping(value = "/freeRides/{date}")
    public ResponseEntity getFreeRides(@PathVariable String date, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.getFreeRides(request, date));
    }
    /*
    @PostMapping
    public ResponseEntity reserveRide(@RequestBody RideReservation reservation, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.reserveRide(request, reservation));
    }*/
    
}
