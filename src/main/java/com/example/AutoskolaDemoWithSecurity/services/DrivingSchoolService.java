
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.constants.RelationshipConstants;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserRelationInfo;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
public class DrivingSchoolService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private ConfirmationRepository confirmationRepository;
    
    @Autowired
    private RelationshipService relationshipService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private NotificationMessageService notificationService;
    
    
    public ResponseEntity createDrivingSchool(DrivingSchoolDTO drivingSchoolDTO) {
        User owner = userRepository.findByEmail(drivingSchoolDTO.getOwnerEmail())
                .orElseThrow(() -> new EntityNotFoundException("No user with email: "+drivingSchoolDTO.getEmail()));
        if(owner.getRoles().contains("OWNER")) {
            DrivingSchool drivingSchool = new DrivingSchool(drivingSchoolDTO);
            drivingSchool.setOwner(owner);

            DrivingSchool school = schoolRepository.save(drivingSchool);
            Relationship r = relationshipRepository.save(new Relationship(school, owner, true));
            if (school == null || r == null) {
                schoolRepository.delete(school);
                relationshipRepository.delete(r);
                throw new PersistenceException("Something went wrong, School " + drivingSchool.getName() + " could not be saved");
            }
            return new ResponseEntity(messageSource.getMessage("school.created", null, Locale.ROOT), HttpStatus.OK);
        }
        return new ResponseEntity("This user has no OWNER role", HttpStatus.BAD_REQUEST);
    }
    
    
    public ResponseEntity getSchoolInfo(int id) throws AccessDeniedException {
        
        DrivingSchool school = schoolRepository.findById(id).get();
        if((school.getOwner().getEmail())
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity(school, HttpStatus.OK);
        } else {
            throw new AccessDeniedException("No permission to access this data");
        }
    }
    
    
    public List<DrivingSchoolDTO> getDrivingSchools() {
        List<DrivingSchool> list = schoolRepository.findAll();
        return list.stream().map(DrivingSchoolDTO::new).collect(Collectors.toList());
    }
    
    
    public List<UserRelationInfo> viewRequests(int relationID){
        DrivingSchool school = relationshipRepository.findById(relationID).get().getDrivingSchool();
        System.out.println("com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService.viewRequests()");
        return confirmationRepository.findAllByDrivingSchool(school)
                .stream()
                .map(UserRelationInfo::new)
                .collect(Collectors.toList());
    }
    
    //ak true - najde relationship - aktivuje ho - vymaze verifikaciu
    //ak false - vymaze verifikaciu, relationship neha, kvoli userovi
    public ResponseEntity confirmUser(int userRelationID, boolean confirm) {
        Relationship relation = relationshipRepository.findById(userRelationID).get();
        if(relation.getDrivingSchool().getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            
            if(confirm) {
                try {
                    relationshipService.activateRelationship(relation, true);
                } catch(Exception e) {
                    throw new EntityNotFoundException("Wrong relationship ID");
                }
            } else {
                relation.setStatus(RelationshipConstants.REJECTED);
                relationshipRepository.save(relation);
            }
            confirmationRepository.deleteByRelation(userRelationID);
            return new ResponseEntity(confirm ? messageSource.getMessage("user.confirmed", null, Locale.ROOT) 
                        : messageSource.getMessage("user.denied", null, Locale.ROOT)
                    , HttpStatus.OK);
        }
        return new ResponseEntity("ID does not belong to your school", HttpStatus.BAD_REQUEST);
    }
    
    
    public ResponseEntity kickUser(int relationshipID) {
        Relationship relation = relationshipRepository.findById(relationshipID).orElseThrow(
                    () -> new EntityNotFoundException("This relationID does not exist"));
        if(relation.getDrivingSchool().getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            
            relation.setActivate(false);
            relation.setStatus(RelationshipConstants.NOT_COMPLETED);
            relationshipRepository.save(relation);
            notificationService.addPushNotification(relation.getUser(), relation.getDrivingSchool()
                    , messageSource.getMessage("user.kicked", null, Locale.ROOT));
        } else {
             return new ResponseEntity("ID does not belong to your school", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(messageSource.getMessage("school.user.kicked", null, Locale.ROOT), HttpStatus.OK);
    }
    
    public List<UserRelationInfo> getAllUsers(int relationID) {
        List<UserRelationInfo> list = new ArrayList<UserRelationInfo>();
        list.addAll(relationshipRepository.findAllByDrivingSchoolAndRoleOrRole(
                relationshipRepository.findById(relationID).get().getDrivingSchool(), "ROLE_STUDENT,", "ROLE_INSTRUCTOR,")
                .stream()
                .map(UserRelationInfo::new)
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .sorted((o1, o2) -> o1.getRole().compareTo(o2.getRole()))
                .collect(Collectors.toList()));
        return list;
    }
    
    /*
    public ResponseEntity completeStudent(int studentRelationID, boolean completed) {
        Relationship relation = relationshipRepository.findById(studentRelationID).orElseThrow(
                    () -> new EntityNotFoundException("This relationID does not exist"));
         if(relation.getDrivingSchool().getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
             if(completed) {
                 if(relation.getStatus().equals(RelationshipConstants.WAITING_FOR_EXAM)) {
                     relationshipService.changeRelationshipStatus(relation, RelationshipConstants.COMPLETED);
                 } else {
                     return new ResponseEntity(messageSource.getMessage("student.rides.notFinished", null, Locale.ROOT), HttpStatus.BAD_REQUEST);
                 }
             } else {
                 relationshipService.changeRelationshipStatus(relation, RelationshipConstants.NOT_COMPLETED);
             }
             
         } else {
             return new ResponseEntity("ID does not belong to your school", HttpStatus.BAD_REQUEST);
         }
        return new ResponseEntity(messageSource.getMessage("student.status.set", null, Locale.ROOT), HttpStatus.OK);
    }
    
    
    public List<UserRelationInfo> getCompletedUsers(int relationID) {
        List<UserRelationInfo> list = new ArrayList<UserRelationInfo>();
        list.addAll(relationshipRepository.findAllByDrivingSchoolAndStatus(
                relationshipRepository.findById(relationID).get().getDrivingSchool(), RelationshipConstants.WAITING_FOR_EXAM)
                .stream()
                .map(UserRelationInfo::new)
                .collect(Collectors.toList()));
        return list;
    }
    */
}
