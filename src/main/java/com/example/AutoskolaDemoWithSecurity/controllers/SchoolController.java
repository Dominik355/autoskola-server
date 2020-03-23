
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.VehicleDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import com.example.AutoskolaDemoWithSecurity.services.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.nio.file.AccessDeniedException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
@RequestMapping(value = {"/school"})
@PreAuthorize("hasRole('ROLE_OWNER')")
@Api(value = "/instructor", description = "Sluzby, ku ktorym musi mat user opravnenie OWNER")
public class SchoolController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
    
    
    @PostMapping(value = {"/addNewSchool"})
    @ApiOperation(value = "${schoolController.createSchool.value}",
            notes = "${schoolController.createSchool.notes}",
            response = ResponseEntity.class)
    public ResponseEntity createSchool(@RequestBody @Valid DrivingSchoolDTO school) {
        return ResponseEntity.ok(schoolService.createDrivingSchool(school));
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
