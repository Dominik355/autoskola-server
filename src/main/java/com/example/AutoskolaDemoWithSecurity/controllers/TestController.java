
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.tests.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/tests"})
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping(value = {"/{id}"})
    public ResponseEntity getTest(@PathVariable("id") int id) {
        return ResponseEntity.ok(testService.getTest(id));
    }
    
    @GetMapping(value = {"/"})
    public ResponseEntity getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }
    
}
