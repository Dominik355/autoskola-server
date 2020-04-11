
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.VerificationToken;
import com.example.AutoskolaDemoWithSecurity.repositories.ConfirmationRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.VerificationTokenRepository;
import com.example.AutoskolaDemoWithSecurity.services.NotificationMessageService;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VerificationsScheduler {
    
    @Autowired
    private VerificationTokenRepository vtr;
    
    @Autowired
    private ConfirmationRepository confirmationRepository;
    
    @Autowired
    private NotificationMessageService messageService;
    
    @Autowired
    private DrivingSchoolRepository schoolRepository;        
    
    Logger log = LoggerFactory.getLogger(VerificationsScheduler.class);
    
    
    @Scheduled(cron = "0 0 3 ? * *")
    @PostConstruct
    private void checkVerificationTokens() {
        List<VerificationToken> tokens = vtr.findAll();
        if(!tokens.isEmpty()) {
            for(VerificationToken token : tokens) {
                if(token.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
                    vtr.delete(token);
                }
            }
        }
    }
    
    @Scheduled(cron = "0 0 19 ? * *")
    private void userConfirmationsCheck() {
        HashSet<Integer> schools = new HashSet<>();
        List<ConfirmUserVerification> requests = confirmationRepository.findAll();
        if(!requests.isEmpty()) {
            for(ConfirmUserVerification ver : requests) {
                schools.add(ver.getDrivingSchool().getId());
            }
        }
        for(Integer id : schools) {
            DrivingSchool school = schoolRepository.findById(id).get();
            messageService.addNotification(school.getOwner(), school
                    , "You still have some unconfirmed users");
        }
    }
    
}
