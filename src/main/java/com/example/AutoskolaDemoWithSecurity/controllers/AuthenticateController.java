/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.otherModels.MyUserDetails;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.AuthenticationRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.AuthenticationResponse;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.ResetPasswordRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserProfileInfo;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.VerificationToken;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import com.example.AutoskolaDemoWithSecurity.services.VerificationTokenService;
import com.example.AutoskolaDemoWithSecurity.utils.JwtUtil;
import java.util.Calendar;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
  
    @Autowired
    private AuthenticationManager authenticationManager;
  
    @Autowired
    private JwtUtil jwtTokenUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerificationTokenService verificationTokenService;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private CompletedRideRepository crr;
    
    
    @RequestMapping(value = {"/registrationConfirm"}, method = {RequestMethod.GET})
    public ResponseEntity confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = this.verificationTokenService.getVerificationToken(token);
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime() <= 0L) {
          this.verificationTokenService.deleteVerificationToken(user);
          //vymaze uzivatela
          return new ResponseEntity("24 hours from registration passed. Register again", HttpStatus.FORBIDDEN);
        }
        this.myUserDetailsService.activateUser(user);
        this.verificationTokenService.deleteVerificationToken(user);
        return new ResponseEntity("Your account has been verified", HttpStatus.OK);
        
    }

    
    @PostMapping({"/forgotPassword"})
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest request) throws InterruptedException {
        String mail = request.getUserEmail();
        System.out.println("Reset password called with email: " + mail);
        return this.myUserDetailsService.resetPassword(mail);
    }
    
    
    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public ResponseEntity createAuthenticationToken
        (@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
            System.out.println("Inside /authenticate/login");
        String email = authenticationRequest.getEmail();
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, authenticationRequest.getPassword()));
        }
        catch (AuthenticationException e) {
            if(userRepository.existsByEmail(authenticationRequest.getEmail())) {
                throw new LoginException("Wrong password");
            } else {
                throw new LoginException("Wrong email");
            }
        }
        
        User user = myUserDetailsService.loadUserWithUsername(authenticationRequest.getEmail());
        UserDetails userDetails = new MyUserDetails(user);  

        String jwt = this.jwtTokenUtil.generateToken(userDetails);

        Relationship relationship = relationshipRepository.findByUser(user);
        return ResponseEntity.ok(new AuthenticationResponse(
                jwt, relationship.getId(), new UserProfileInfo(user, crr.findAllByRelationship(relationship).size())));
}


    @RequestMapping(value = {"/register"}, method = {RequestMethod.POST})
    public ResponseEntity addNewUser(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request) {
        return ResponseEntity.ok(this.myUserDetailsService.addNewUser(userDTO, request)); 
    }
    
}
