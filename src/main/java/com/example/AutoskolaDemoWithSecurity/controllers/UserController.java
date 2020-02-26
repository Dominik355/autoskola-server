
package com.example.AutoskolaDemoWithSecurity.controllers;


import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdateEmailRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdatePasswordRequest;
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





@RestController
@RequestMapping("/update")
public class UserController
{
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    
    @PostMapping({"/password"})
    public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updatePassword(updatePasswordRequest)); 
    }

    
    @PostMapping({"/email"})
    public ResponseEntity updateEmail(@RequestBody @Valid UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updateEmail(updateEmailRequest)); 
    }

}