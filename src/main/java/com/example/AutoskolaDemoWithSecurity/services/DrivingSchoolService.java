
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.nio.file.AccessDeniedException;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    
    
    public ResponseEntity createDrivingSchool(DrivingSchool drivingSchool) {
        User owner = (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        drivingSchool.setOwner(owner);
        DrivingSchool school = schoolRepository.save(drivingSchool);
        Relationship r = relationshipRepository.save(new Relationship(school, owner, true));
        if (school == null || r == null) {
            schoolRepository.delete(school);
            relationshipRepository.delete(r);
        throw new PersistenceException("Something went wrong, School " + drivingSchool.getName() + " could not be saved");
        }
        return ResponseEntity.ok("School succesfully created");
    }
    
    public ResponseEntity getSchoolInfo(int id) throws AccessDeniedException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        DrivingSchool school = schoolRepository.findById(id).orElseThrow();
        if((school.getOwner().getEmail()).equals(currentUser.getName())) {
            return ResponseEntity.ok(school);
        } else {
            throw new AccessDeniedException("No permission to access this data");
        }
    }
    
    
    
}
