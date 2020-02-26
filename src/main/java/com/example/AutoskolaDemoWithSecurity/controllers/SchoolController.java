
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import java.nio.file.AccessDeniedException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@PreAuthorize("hasRole('ROLE_OWNER')")
public class SchoolController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @PostMapping(value = {"/addNewSchool"})
    public ResponseEntity createSchool(@RequestBody @Valid DrivingSchool school) {
        return ResponseEntity.ok(schoolService.createDrivingSchool(school));
    }
    
    @GetMapping(value = {"/getSchoolInfo/{schoolID}"})
    public ResponseEntity getSchoolInfo(@PathVariable int id) throws AccessDeniedException {
        return ResponseEntity.ok(schoolService.getSchoolInfo(id));
    }
    
}
