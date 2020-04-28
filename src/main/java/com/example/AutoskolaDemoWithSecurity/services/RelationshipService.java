
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.CustomLoginException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.constants.RelationshipConstants;
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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
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
    
    @Autowired
    private MessageSource messageSource;
    
    
    public ResponseEntity newRelationship(int schoolID, int userID) {
        //vytvori sa novy objekt, neaktivny + sa vytvori novy confirmation - a ksa ten potom potvrdi, v inej metode, toto sa len aktivuje
        DrivingSchool ds = schoolRepository.findById(schoolID)
                .orElseThrow(() -> new EntityNotFoundException("This school does not exists!"));

        User user = userRepository.findById(userID).get();
        
        Relationship relationship = new Relationship(ds, user, false);
        String info = "undefined";

        if(user.getRoles().contains("STUDENT")) {
            info = "New student "+user.getFullName();
        } else if(user.getRoles().contains("INSTRUCTOR")) {
            info = "New instructor "+user.getFullName();
        }
        Relationship rs = relationshipRepository.save(relationship);
        confirmationRepository.save(new ConfirmUserVerification(ds, user, info
                , relationshipRepository.findByUserAndDrivingSchool(user, ds).getId()));
        
        return ResponseEntity.ok(messageSource.getMessage("relationship.created", null, Locale.ROOT));
    }
    
    
    public void activateRelationship(Relationship relationship, boolean active) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setActivate(active);
        this.relationshipRepository.save(changedRelationship);
        if(active) {
            messageService.addNotification(relationship, messageSource.getMessage("school.welcome", null, Locale.ROOT));
        }
    }
    
    
    public void changeRelationshipStatus(Relationship relationship, String status) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setStatus(status);
        if(status.equals(RelationshipConstants.COMPLETED)) {
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
                     response.add(new UserRelationInfo(relation, RelationshipConstants.ACTIVE));
                 } else {
                     Optional<ConfirmUserVerification> verification = confirmationRepository.findByRelation(relation.getId());
                     if(verification.isPresent()) {
                         response.add(new UserRelationInfo(relation, RelationshipConstants.WAITING));
                     } else {
                         if(relation.getStatus().equals(RelationshipConstants.COMPLETED)) {
                             response.add(new UserRelationInfo(relation, RelationshipConstants.COMPLETED));
                         } else {
                             response.add(new UserRelationInfo(relation, RelationshipConstants.REJECTED));
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
    
    
    public ResponseEntity getCompletedRelationship(int relationID) {
        User user = userService.loadUserWithUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Relationship relationship = relationshipRepository.findById(relationID).get();
        if(relationship.getUser().equals(user)) {
            if(relationship.getStatus().equals(RelationshipConstants.COMPLETED)) {
                return new ResponseEntity(new CompletedRelationship(
                        relationship.getCreationDate()
                        , relationship.getEndingdate()
                        , crr.findAllByStudentAndDrivingSchoolAndStatus(
                        relationship.getUser(), relationship.getDrivingSchool(), "FINISHED")
                        .stream().map(RideDTO::new).collect(Collectors.toList()))
                        , HttpStatus.OK);
            }
            else {
                return new ResponseEntity(messageSource.getMessage("relationship.notEnded", null, Locale.ROOT), HttpStatus.BAD_REQUEST);
            }
        }
        throw new CustomLoginException("This relationID is not yours");
    }
    
}
