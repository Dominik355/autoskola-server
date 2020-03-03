
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    
    
    public ResponseEntity newRelationship(int schoolID, int userID) {
        //vytvori sa novy objekt, neaktivny + sa vytvori novy confirmation - a ksa ten potom potvrdi, v inej metode, toto sa len aktivuje
        DrivingSchool ds = schoolRepository.findById(schoolID)
                .orElseThrow(() -> new EntityNotFoundException("This school does not exists!"));

        User user = userRepository.findById(userID).get();
        
        //potom zmenit na false, a obnovit tu verifikaciu, zatial to staci ale takto
        Relationship relationship = new Relationship(ds, user, true);
        /*
        String info = "undefined";
        if(user.getRoles().contains("USER")) {
            info = "New student "+user.getName()+" "+user.getSurname();
        } else if(user.getRoles().contains("INSTRUCTOR")) {
            info = "New instructor "+user.getName()+" "+user.getSurname();
        }
        ConfirmUserVerification verification = new ConfirmUserVerification(ds, user, info);
        */
        Relationship rs = relationshipRepository.save(relationship);
        /*
        ConfirmUserVerification cuv = confirmationRepository.save(verification);
        if(rs == null || cuv == null) {
            throw new PersistenceException("Something went wrong, relationship was not created");
        }*/
        return ResponseEntity.ok("Relationship created, wait for confirmation");
    }
    
    public void activateRelationship(Relationship relationship) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setActivate(true);
        this.relationshipRepository.save(changedRelationship);
    }
    
}
