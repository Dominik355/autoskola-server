
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import io.swagger.annotations.ApiOperation;
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
    
    @Autowired
    private UserRepository userRepository;
    
    
    @GetMapping(value = "/schoolsAvailable")
    @ApiOperation(value = "zoznam skol - len informacne")
    public ResponseEntity getDrivingSchools() {
        return ResponseEntity.ok(schoolService.getDrivingSchools());
    }
    
    @GetMapping(value = "/linksAvailable")
    @ApiOperation(value = "zoznam linkov - len informacne")
    public ResponseEntity getAvailableLinks() {
        Object[] mappedUrls = requestMappingHandlerMapping.getHandlerMethods().keySet().toArray();
        return ResponseEntity.ok(mappedUrls);
    }
    
    @ApiOperation(value = "zoznam uzivatelov - len informacne")
    @GetMapping(value = "/listOfUsers")
    public ResponseEntity getListOfUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    
}
