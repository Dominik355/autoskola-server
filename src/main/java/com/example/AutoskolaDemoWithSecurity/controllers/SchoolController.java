
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import io.swagger.annotations.ApiOperation;
import java.nio.file.AccessDeniedException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = {"/school"})
@PreAuthorize("hasRole('ROLE_OWNER')")
public class SchoolController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
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
    
}
