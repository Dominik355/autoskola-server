
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.VehicleDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import com.example.AutoskolaDemoWithSecurity.services.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.nio.file.AccessDeniedException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = {"/school"})
@PreAuthorize("hasRole('ROLE_OWNER')")
@Api(value = "/school", description = "Sluzby, ku ktorym musi mat user opravnenie OWNER")
public class SchoolController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
    
    @Autowired
    private RelationshipRepository relationRepository;
      
    
    @GetMapping({"/getRequests"})
    public ResponseEntity getRequests(HttpServletRequest request) {
        return new ResponseEntity(schoolService.viewRequests(request.getIntHeader("Relation")), HttpStatus.OK);
    }
    
    @GetMapping({"/getCompletedStudents"})
    public ResponseEntity getCompletedStudents(HttpServletRequest request) {
        return new ResponseEntity(schoolService.getCompletedUsers(request.getIntHeader("Relation")), HttpStatus.OK);
    }
    
    @PostMapping(value = {"/confirmUser/{userRelationID}"})
    public ResponseEntity confirmUser(@PathVariable int userRelationID
            , @RequestParam(required = true) boolean confirm) {
        return ResponseEntity.ok(schoolService.confirmUser(userRelationID, confirm));
    }
    
    @PostMapping(value = {"/completeStudent/{userRelationID}"})
    public ResponseEntity completeStudent(@PathVariable int userRelationID
            , @RequestParam(required = true) boolean complete) {
        return new ResponseEntity(schoolService.completeStudent(userRelationID, complete), HttpStatus.OK);
    }
    
    @GetMapping(value = "/getSchoolInfo/{id}")
    @ApiOperation(value = "${schoolController.getSchoolInfo.value}",
            notes = "${schoolController.getSchoolInfo.notes}",
            response = InstructorRides.class,
            responseContainer = "List")
    public ResponseEntity getSchoolInfo(@PathVariable int id) throws AccessDeniedException {
        System.out.println("School controller - getSchoolInfo()");
        return ResponseEntity.ok(schoolService.getSchoolInfo(id));
    }
    
    @PostMapping(value = {"/addVehicle"})
    public ResponseEntity addVehicle(@RequestBody @Valid VehicleDTO vehicleDTO, HttpServletRequest request) {
        return ResponseEntity.ok(vehicleService.addVehicle(vehicleDTO, request.getIntHeader("Relation")));
    }
    
    @DeleteMapping(value = {"/removeVehicle/{vehicleID}"})
    public ResponseEntity removeVehicle(@PathVariable("vehicleID") int vehicleID) {
        return ResponseEntity.ok(vehicleService.removeVehicle(vehicleID));
    }
    
    @PostMapping(value = {"/modifyVehicle"})
    public ResponseEntity modifyVehicle(@RequestBody @Valid VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.modifyVehicle(vehicleDTO));
    }
    
    @GetMapping(value = {"/getVehicles/{vehicleID}"})
    public ResponseEntity getVehicle(@PathVariable("vehicleID") int vehicleID, HttpServletRequest request) {
        return ResponseEntity.ok(vehicleService.getVehicle(request.getIntHeader("Relation"), vehicleID));
    }
    
    @GetMapping(value = {"/getVehicles"})
    public ResponseEntity getVehicles(HttpServletRequest request) {
        return ResponseEntity.ok(vehicleService.getVehicles(request.getIntHeader("Relation")));
    }
    
}
