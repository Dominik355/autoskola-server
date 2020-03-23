
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.VerificationToken;
import com.example.AutoskolaDemoWithSecurity.repositories.VerificationTokenRepository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


public class VerificationTokensScheduler {
    
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    Logger log = LoggerFactory.getLogger(VerificationTokensScheduler.class);
    
    @Scheduled(fixedRate = 60000)
    private void checkTokensExperation() {
        
        List<VerificationToken> tokens = tokenRepository.findAll();
        ExecutorService executor = Executors.newCachedThreadPool();
        
        for(VerificationToken token : tokens) {
            executor.execute(() -> {         
                Date now = new Timestamp(System.currentTimeMillis());
                Date tokenDate = token.getExpiryDate();
                if(now.getTime() > tokenDate.getTime()) {
                    //token expiroval - vymazat ho a lognut 
                    log.info("Removed expired token"+token.toString());
                }           
            });
        }
        executor.shutdown();
        
    }
    
}
