
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
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
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
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
            return new ResponseEntity("School succesfully created, wait for", HttpStatus.OK);
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
                relation.setStatus("rejected");
                relationshipRepository.save(relation);
            }
            confirmationRepository.deleteByRelation(userRelationID);
            return new ResponseEntity(confirm ? "User confirmed" : "User rejected", HttpStatus.OK);
        }
        return new ResponseEntity("ID does not belong to your school", HttpStatus.BAD_REQUEST);
    }
    
    
    public ResponseEntity completeStudent(int studentRelationID, boolean completed) {
        Relationship relation = relationshipRepository.findById(studentRelationID).orElseThrow(
                    () -> new EntityNotFoundException("This relationID does not exist"));
        System.out.println("com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService.completeStudent()");
         if(relation.getDrivingSchool().getOwner().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
             System.out.println("com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService.completeStudent()");
             if(completed) {
                 System.out.println("com.example.AutoskolaDemoWithSecurity.services.Drivin          TRUE    ");
                 if(relation.getStatus().equalsIgnoreCase("waiting for exam")) {
                     relationshipService.changeRelationshipStatus(relation, "completed");
                 } else {
                     return new ResponseEntity("This user did not completed rides yet", HttpStatus.BAD_REQUEST);
                 }
             } else {
                 System.out.println("com.example.AutoskolaDemoWithSecurity.services.            FALSE");
                 relationshipService.changeRelationshipStatus(relation, "not completed");
             }
             
         } else {
             return new ResponseEntity("ID does not belong to your school", HttpStatus.BAD_REQUEST);
         }
        return new ResponseEntity("Student status set as: "+(completed ? "completed" : "not completed"), HttpStatus.OK);
    }
    
    public List<UserRelationInfo> getCompletedUsers(int relationID) {
        List<UserRelationInfo> list = new ArrayList<UserRelationInfo>();
        list.addAll(relationshipRepository.findAllByDrivingSchoolAndStatus(
                relationshipRepository.findById(relationID).get().getDrivingSchool(), "waiting for exam")
                .stream()
                .map(UserRelationInfo::new)
                .collect(Collectors.toList()));
        return list;
    }
    
}
