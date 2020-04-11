
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserConfirmation;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    
    
    public ResponseEntity newRelationship(int schoolID, int userID) {
        //vytvori sa novy objekt, neaktivny + sa vytvori novy confirmation - a ksa ten potom potvrdi, v inej metode, toto sa len aktivuje
        DrivingSchool ds = schoolRepository.findById(schoolID)
                .orElseThrow(() -> new EntityNotFoundException("This school does not exists!"));

        User user = userRepository.findById(userID).get();
        
        Relationship relationship = new Relationship(ds, user, false);
        if(user.getRoles().contains("STUDENT")) {
            relationship.setStatus("waiting for rides completion");
        }
        
        String info = "undefined";
        if(user.getRoles().contains("STUDENT")) {
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
    
    public void activateRelationship(Relationship relationship) {
        Relationship changedRelationship = relationshipRepository.getOne(relationship.getId());
        changedRelationship.setActivate(true);
        this.relationshipRepository.save(changedRelationship);
        messageService.addNotification(relationship, "Welcome to our school, now you can manage your rides");
    }
    
    //pozrie na relationship,ak neaktivny a ak k nemu existuje confiration - tak ho vrati s infrmaciou pending, ak neexistuje- informacia rejected
    public List<UserConfirmation> viewUserRequests() {
        List<UserConfirmation> confirmations = new ArrayList<>();
        User user = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<Relationship> relations = relationshipRepository.findAllByUser(user);
        
        if(!relations.isEmpty()) {
            for(Relationship relation : relations) {
                if(!relation.isActivate()) {
                    Optional<ConfirmUserVerification> verification = confirmationRepository.findByRelation(relation.getId());
                    if(verification.isPresent()) {
                        confirmations.add(new UserConfirmation(verification.get(), "waiting"));
                    } else {
                        confirmations.add(new UserConfirmation(
                                user.getFullName(),
                                "rejected",
                                relation.getDrivingSchool().getName(),
                                relation.getId()));
                    }
                }
            }
        }
        return confirmations;
    }
    
}
