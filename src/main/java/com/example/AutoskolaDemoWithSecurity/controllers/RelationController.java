
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.otherModels.MyUserDetails;
import com.example.AutoskolaDemoWithSecurity.services.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = {"/relationship"})
@PreAuthorize("hasRole('ROLE_INSTRUCTOR') or hasRole('ROLE_STUDENT')")
public class RelationController {
    
    @Autowired
    private RelationshipService relationshipService;
    
    @PostMapping(value = "/enterSchool/{schoolID}")
    public ResponseEntity enterSchool(@PathVariable("schoolID") int id) {
        
        int userID = (int) ((MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getId();
        
        return ResponseEntity.ok(relationshipService.newRelationship(id, userID));
        
    }
    
}
