
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.services.CompletedRideService;
import com.example.AutoskolaDemoWithSecurity.services.RideService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/instructor")
@PreAuthorize("hasRole('ROLE_INSTRUCTOR') or hasRole('ROLE_OWNER')")
public class InstructorController {
    
    @Autowired
    private RideService rideService;
    
    @Autowired
    private CompletedRideService crs;
    
    @Autowired
    private UserRepository userRepository; 
    
    
    @GetMapping(value = {"/"})
    public String helloInstructor() {
        return "Hello instructor";
    }
    
    @PostMapping(value = "/addRide")
    public ResponseEntity addRide (@RequestBody @Valid RideDTO ride, HttpServletRequest request) {
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(rideService.isRideDTOFine(ride, instructor)) {
            return ResponseEntity.ok(rideService.addRide(ride, request.getIntHeader("Relation"), instructor));
        } else {
            return new ResponseEntity("Ride could not be created", HttpStatus.BAD_REQUEST);
        }
    }
    
    //@valid na pole tych objketov nefunguje
    @PostMapping(value = "/addRides")
    public ResponseEntity addRide (@RequestBody RideDTO[] rides, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.addRides(rides, request.getIntHeader("Relation")));
    }
    
    @DeleteMapping(value = "/removeRide/{rideID}")
    public ResponseEntity removeRide(@PathVariable("rideID") int id, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.removeRide(id, request.getIntHeader("Relation")));
    }
    
    @GetMapping(value = {"/getCompletedRides/{inputDate}"})
    public ResponseEntity getCompletedRides (@PathVariable("inputDate") String inputDate) {
        String date;
        if(!inputDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            date = inputDate.substring(0, inputDate.indexOf("T"));
        } else {
            date = inputDate;
        }
        return ResponseEntity.ok(crs.getCompletedRides(date));
    }
    
    @GetMapping(value = {"/getLastRides"})
    public ResponseEntity getLastRides (@RequestParam int count) {
        return ResponseEntity.ok(crs.getLastRides(count));
    }
    
    @PostMapping(value = {"/completeRide"})
    public ResponseEntity completeRide (@RequestBody @Valid RideDTO ride, HttpServletRequest request) {
        return ResponseEntity.ok(crs.completeRide(ride));
    }
    
}
