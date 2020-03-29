
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


public class NotificationsScheduler {
    
    Logger log = LoggerFactory.getLogger(NotificationsScheduler.class);
    
    @Scheduled(fixedRate = 30000)
    @Async
    private void checkRideStatus() {
        
    }
    
}
