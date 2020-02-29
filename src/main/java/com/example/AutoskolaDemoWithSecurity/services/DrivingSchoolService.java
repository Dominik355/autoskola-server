
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    
    
    public ResponseEntity createDrivingSchool(DrivingSchoolDTO drivingSchoolDTO) {
            User owner = (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        System.out.println("owner's email: "+owner.getEmail());
        DrivingSchool drivingSchool = new DrivingSchool(drivingSchoolDTO);
        drivingSchool.setOwner(owner);

            DrivingSchool school = schoolRepository.save(drivingSchool);
            Relationship r = relationshipRepository.save(new Relationship(school, owner, true));

        if (school == null || r == null) {
            System.out.println("1 of the values is null, school: "+school+", relationship: "+r);
            schoolRepository.delete(school);
            relationshipRepository.delete(r);
        throw new PersistenceException("Something went wrong, School " + drivingSchool.getName() + " could not be saved");
        }
        return new ResponseEntity("School succesfully created", HttpStatus.OK);
        
    }
    
    public ResponseEntity getSchoolInfo(int id) throws AccessDeniedException {
        System.out.println("1");
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("2");

        DrivingSchool school = schoolRepository.findById(id).get();
                System.out.println("3");

        if((school.getOwner().getEmail()).equals(currentUser.getName())) {
                    System.out.println("4");

            return new ResponseEntity(id, HttpStatus.OK);
        } else {
                    System.out.println("5");

            throw new AccessDeniedException("No permission to access this data");
        }
        
    }
    
    public List<DrivingSchoolDTO> getDrivingSchools() {
        List<DrivingSchool> list = schoolRepository.findAll();
        return list.stream().map(DrivingSchoolDTO::new).collect(Collectors.toList());
    }
    
}
