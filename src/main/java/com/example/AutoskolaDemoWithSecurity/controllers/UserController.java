
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdateEmailRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdatePasswordRequest;
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import com.example.AutoskolaDemoWithSecurity.services.RideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "Dostupne pre vsetkych prihlasenych uzivatelov")
@RequestMapping(value = {"/user"})
public class UserController
{
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private RideService rideService;
    
    
    @PostMapping({"/update/password"})
    @ApiOperation(value = "${userController.updatePassword.value}",
            response = ResponseEntity.class)
    public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updatePassword(updatePasswordRequest)); 
    }

    
    @PostMapping({"/update/email"})
    @ApiOperation(value = "${userController.updateEmail.value}",
            response = ResponseEntity.class)
    public ResponseEntity updateEmail(@RequestBody @Valid UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updateEmail(updateEmailRequest)); 
    }

}