
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.tests.TestService;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = {"/tests"})
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping(value = {"/{id}"})
    @ApiOperation(value = "${testController.getAllTests.value}",
                response = ResponseEntity.class)
    public ResponseEntity getTest(@PathVariable("id") int id) {
        return ResponseEntity.ok(testService.getTest(id));
    }
    
    @GetMapping(value = {"/"})
    @ApiOperation(value = "${testController.getTest.value}",
                response = ResponseEntity.class,
                responseContainer = "List")
    public ResponseEntity getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }
    
    @PostMapping("/uploadImage")
    public ResponseEntity uploadImage(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(testService.saveImage(imageFile));
    }
    
}
