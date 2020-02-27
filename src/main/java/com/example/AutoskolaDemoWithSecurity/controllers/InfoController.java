
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @GetMapping(value = "/schoolsAvailable")
    public ResponseEntity getDrivingSchools() {
        return ResponseEntity.ok(schoolService.getDrivingSchools());
    }
    
}
