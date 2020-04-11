
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.otherModels.MyUserDetails;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.services.NotificationMessageService;
import com.example.AutoskolaDemoWithSecurity.services.RelationshipService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @Autowired
    private RelationshipRepository rr;
    
    @Autowired
    private NotificationMessageService messageService;
    
    
    @PostMapping(value = "/enterSchool/{schoolID}")
    @ApiOperation(value = "${relationController.enterSchool.value}",
            notes = "${relationController.enterSchool.notes}",
            response = ResponseEntity.class)
    public ResponseEntity enterSchool(@ApiParam(value = "${relationController.enterSchool.paramValue}", required = true)
                @PathVariable("schoolID") int id) {
        
        int userID = (int) ((MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getId();
        
        return ResponseEntity.ok(relationshipService.newRelationship(id, userID));
        
    }

    @GetMapping(value = {"/getNotifications"})
    @ApiOperation(value = "${relationshipController.getNotifications.value}")
    public ResponseEntity getNotifications(HttpServletRequest request) {
        return ResponseEntity.ok(messageService.getNotifications(request.getIntHeader("Relation")));
    }
    
}
