
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.services.CancelledRideService;
import com.example.AutoskolaDemoWithSecurity.services.CompletedRideService;
import com.example.AutoskolaDemoWithSecurity.services.RideService;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.RideDateConstraint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Api(value = "/instructor", description = "Sluzby, ku ktorym musi mat user opravnenie INSTRUCTOR alebo OWNER")
public class InstructorController {
    
    @Autowired
    private RideService rideService;
    
    @Autowired
    private CompletedRideService crs;
    
    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private CancelledRideService clrs;
    
    
    @GetMapping(value = {"/"})
    public String helloInstructor() {
        return "Hello instructor";
    }
    
    
    @PostMapping(value = "/addRide")
    @ApiOperation(value = "${instructorController.addRide.value}",
                notes = "${instructorController.addRide.notes}",
                response = ResponseEntity.class)
    public ResponseEntity addRide (@ApiParam(value = "${instructorController.addRide.paramValue}")
                @RequestBody @Valid RideDTO ride, HttpServletRequest request) throws ParseException {
        
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(rideUtil.isRideDTOFine(ride, instructor)) {
            return ResponseEntity.ok(rideService.addRide(ride, request.getIntHeader("Relation"), instructor));
        } else {
            return new ResponseEntity("Ride could not be created", HttpStatus.BAD_REQUEST);
        }
    }
    
    
    //@valid na pole tych objketov nefunguje
    @PostMapping(value = "/addRides")
    @ApiOperation(value = "${instructorController.addRides.value}",
                notes = "${instructorController.addRides.notes}",
                response = ResponseEntity.class)
    public ResponseEntity addRides (@ApiParam(value = "${instructorController.addRides.paramValue}")
                @RequestBody @Valid RideDTO[] rides, HttpServletRequest request) throws ParseException {
        return ResponseEntity.ok(rideService.addRides(rides, request.getIntHeader("Relation")));
    }
    
    
    @GetMapping(value = {"/myRides"})
    public ResponseEntity getMyRides(@RequestParam(defaultValue = "") String date, HttpServletRequest request) {
        if(date.equals("") || rideUtil.isDateValid(date)) {
            return rideService.getMyRides(request.getIntHeader("Relation"), date);
        }
        return new ResponseEntity("Wrong date", HttpStatus.BAD_REQUEST);
    }
    
    
    @DeleteMapping(value = "/removeRides")
    @ApiOperation(value = "${instructorController.removeRides.value}",
                notes = "${instructorController.removeRides.notes}",
                response = ResponseEntity.class)
    public ResponseEntity removeRides(@ApiParam(value = "${instructorController.removeRides.paramValue}")
                 @RequestBody @Valid RideDTO[] rides, HttpServletRequest request) throws ParseException {
        return ResponseEntity.ok(clrs.removeRides(rides, request.getIntHeader("Relation")));
    }
    
    
    @GetMapping(value = {"/getCompletedRides"})
    @ApiOperation(value = "${instructorController.getCompletedRides.value}",
                notes = "${instructorController.getCompletedRides.notes}",
                response = ResponseEntity.class,
                responseContainer = "List")
    public ResponseEntity getCompletedRides (@ApiParam(value = "${instructorController.getCompletedRides.paramValue}")
                @RequestParam(name = "date", defaultValue = "") String date, HttpServletRequest request) {
        if(date.equals("") || rideUtil.isDateValid(date)){
            String inputDate;
            if(date.contains("T")) {
                inputDate = date.substring(0, date.indexOf("T"));
            } else {
                inputDate = date;
            }
            return ResponseEntity.ok(crs.getCompletedRides(request.getIntHeader("Relation"), inputDate));
        }
        return new ResponseEntity("Invalid date", HttpStatus.BAD_REQUEST);
    }
    
    //default value nastavena, aby ked nieje zadana ,vratia sa vsetky jazdy z poslednych 14 dni
    @GetMapping(value = {"/getLastRides"})
    @ApiOperation(value = "${instructorController.getLastRides.value}",
            notes = "${instructorController.getLastRides.notes}",
            response = RideDTO.class,
            responseContainer = "List")
    public ResponseEntity getLastRides (@ApiParam(value = "${instructorController.getLastRides.paramValue}")
            @RequestParam(defaultValue = "1000") int count, HttpServletRequest request) {
        return ResponseEntity.ok(crs.getLastRides(count, request.getIntHeader("Relation")));
    }
    
    
    @PostMapping(value = {"/completeRide"})
    @ApiOperation(value = "${instructorController.completeRide.value}",
            notes = "${instructorController.completeRide.notes}",
            response = ResponseEntity.class)
    public ResponseEntity completeRide (@ApiParam(value = "${instructorController.completeRide.paramValue}", required = true)
            @RequestBody @Valid RideDTO rideDTO, HttpServletRequest request) throws ParseException {
        return ResponseEntity.ok(crs.completeRide(rideDTO, request.getIntHeader("Relation")));
    }
    
    @GetMapping(value = {"/showTimes/{inputDate}"})
    @ApiOperation(value = "${instructorController.showTimes.value}",
            notes = "${instructorController.showTimes.notes}",
            response = ResponseEntity.class)
    public ResponseEntity showTimes(@PathVariable("inputDate") @RideDateConstraint String inputDate, HttpServletRequest request) {
        return ResponseEntity.ok(rideService.showTimes(inputDate, request.getIntHeader("Relation")));
    }
    
}
