
package com.example.AutoskolaDemoWithSecurity.controllers;


import com.example.AutoskolaDemoWithSecurity.models.otherModels.MyUserDetails;
import com.example.AutoskolaDemoWithSecurity.services.CompletedRideService;
import com.example.AutoskolaDemoWithSecurity.services.RideService;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import io.swagger.annotations.ApiOperation;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentController {
        
    Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    @Autowired
    private  RideService rideService;
    
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private CompletedRideService crs;
    
    
    @GetMapping(value = {"/hello"})
    public String helloStudent() {
        MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello student, your ID is: "+details.getId();
    }
    
    @GetMapping(value = "/freeRides/{date}")
    @ApiOperation(value = "${studentController.getFreeRides.value}",
            notes = "${studentController.getFreeRides.notes}",
            response = ResponseEntity.class)
    public ResponseEntity getFreeRides(@PathVariable String date, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.getFreeRides(request, date));
    }
    
    @PostMapping("/reserveRide/{rideID}")
    public ResponseEntity reserveRide(@PathVariable("rideID") int rideID, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.reserveRide(request.getIntHeader("Relation"), rideID));
    }
    
    @PostMapping("/cancelRide/{rideID}")
    public ResponseEntity cancelRide(@PathVariable("rideID") int rideID, HttpServletRequest request) throws ParseException {
        return ResponseEntity.ok(rideService.cancelRide(request.getIntHeader("Relation"), rideID));
    }
    
    @GetMapping(value = {"/getReservedRides"})
    public ResponseEntity getReservedRides(@RequestParam(defaultValue = "") String date, HttpServletRequest request) {
        if(date.equals("") || rideUtil.isDateValid(date)) {
            return rideService.getMyRides(request.getIntHeader("Relation"), date);
        }
        return new ResponseEntity("Wrong date", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping(value = {"/getCompletedRides"})
    public ResponseEntity getCompletedRides(@RequestParam(defaultValue = "") String date, HttpServletRequest request) {
        if(date.equals("") || rideUtil.isDateValid(date)) {
            return new ResponseEntity(
                    crs.getCompletedRides(request.getIntHeader("Relation"), date), HttpStatus.OK);
        }
        return new ResponseEntity("Wrong date", HttpStatus.BAD_REQUEST);
    }
    
}
