
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.CustomLoginException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.CompletedRelationship;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserProfileInfo;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserRelationInfo;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RelationshipService {
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
    
    @Autowired
    private ConfirmationRepository confirmationRepository;
    
    @Autowired
    private NotificationMessageService messageService;
    
    @Autowired
    private CompletedRideRepository crr;
    
    @Autowired
    private MyUserDetailsService userService;
    
    
    public ResponseEntity newRelationship(int schoolID, int userID) {
        //vytvori sa novy objekt, neaktivny + sa vytvori novy confirmation - a ksa ten potom potvrdi, v inej metode, toto sa len aktivuje
        DrivingSchool ds = schoolRepository.findById(schoolID)
                .orElseThrow(() -> new EntityNotFoundException("This school does not exists!"));

        User user = userRepository.findById(userID).get();
        
        Relationship relationship = new Relationship(ds, user, false);
        String info = "undefined";

        if(user.getRoles().contains("STUDENT")) {
            relationship.setStatus("waiting for rides completion");
            info = "New student "+user.getFullName();
        } else if(user.getRoles().contains("INSTRUCTOR")) {
            info = "New instructor "+user.getFullName();
        }
        Relationship rs = relationshipRepository.save(relationship);
        ConfirmUserVerification verification = new ConfirmUserVerification(ds, user, info
                , relationshipRepository.findByUserAndDrivingSchool(user, ds).getId());
        
        ConfirmUserVerification cuv = confirmationRepository.save(verification);
        if(rs == null || cuv == null) {
            try {
                relationshipRepository.delete(rs);
                confirmationRepository.delete(cuv);
            } catch (Exception e) {}
            throw new PersistenceException("Something went wrong, relationship was not created");
        }
        return ResponseEntity.ok("Relationship created, wait for confirmation");
    }
    
    public void activateRelationship(Relationship relationship, boolean active) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setActivate(active);
        this.relationshipRepository.save(changedRelationship);
        if(active) {
            messageService.addNotification(relationship, "Welcome to our school, now you can manage your rides");
        }
    }
    
    public void changeRelationshipStatus(Relationship relationship, String status) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setStatus(status);
        if(status.contains("completed")) {
            changedRelationship.setEndingdate(new Timestamp(System.currentTimeMillis()));
            activateRelationship(relationship, false);
        }
        this.relationshipRepository.save(changedRelationship);
    }
    
    
    public List<UserRelationInfo> getAllRelations(String userEmail) {
         List<UserRelationInfo> response = new ArrayList<>();
         User user;
         try{
             user = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
         } catch (Exception e) {
             user = userRepository.findByEmail(userEmail).get();
         } 
         List<Relationship> relations = relationshipRepository.findAllByUser(user);
         if(!relations.isEmpty()) {
             for(Relationship relation : relations) {
                 if(relation.isActivate()) {
                     response.add(new UserRelationInfo(relation, "active"));
                 } else {
                     Optional<ConfirmUserVerification> verification = confirmationRepository.findByRelation(relation.getId());
                     if(verification.isPresent()) {
                         response.add(new UserRelationInfo(relation, "waiting"));
                     } else {
                         if(relation.getStatus().equalsIgnoreCase("completed")) {
                             response.add(new UserRelationInfo(relation, "completed"));
                         } else {
                             response.add(new UserRelationInfo(relation, "rejected"));
                         }
                     }
                 }
             }
         }
         return response;
    }
    
    
    public UserProfileInfo getRelationInfo(int relationID) {
        Relationship relationship = relationshipRepository.findById(relationID).get();
        int ridesCompleted = 0;
        try {
            if(relationship.getRole().contains("STUDENT")) {
                ridesCompleted = crr.findAllByStudentAndDrivingSchoolAndStatus(
                        relationship.getUser(), relationship.getDrivingSchool(), "FINISHED").size();
            } else {
                ridesCompleted = crr.findAllByInstructorAndDrivingSchoolAndStatus(
                        relationship.getUser(), relationship.getDrivingSchool(), "FINISHED").size();
            }
        } catch (Exception e) { ridesCompleted = 0; }
        
        return new UserProfileInfo(relationship.getUser(), ridesCompleted);
    }
    
    public CompletedRelationship getCompletedRelationship(int relationID) {
        User user = userService.loadUserWithUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Relationship relationship = relationshipRepository.findById(relationID).get();
        if(relationship.getUser().equals(user)) {
            List<RideDTO> rides = crr.findAllByStudentAndDrivingSchoolAndStatus(
                    relationship.getUser(), relationship.getDrivingSchool(), "FINISHED")
                    .stream().map(RideDTO::new).collect(Collectors.toList());
            return new CompletedRelationship(
                        relationship.getCreationDate()
                        , relationship.getEndingdate()
                        , crr.findAllByStudentAndDrivingSchoolAndStatus(
                        relationship.getUser(), relationship.getDrivingSchool(), "FINISHED")
                        .stream().map(RideDTO::new).collect(Collectors.toList()));
        }
        throw new CustomLoginException("This relationID is not yours");
    }
    
}
