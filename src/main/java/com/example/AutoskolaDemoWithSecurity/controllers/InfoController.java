
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
@RequestMapping("/info")
public class InfoController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    
    
    @GetMapping(value = "/schoolsAvailable")
    public ResponseEntity getDrivingSchools() {
        return ResponseEntity.ok(schoolService.getDrivingSchools());
    }
    
    @GetMapping(value = "/linksAvailable")
    public ResponseEntity getAvailableLinks() {
        Object[] mappedUrls = requestMappingHandlerMapping.getHandlerMethods().keySet().toArray();
        return ResponseEntity.ok(mappedUrls);
    }

    
}
