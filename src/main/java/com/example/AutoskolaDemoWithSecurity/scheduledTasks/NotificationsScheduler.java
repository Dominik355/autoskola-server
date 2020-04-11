
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.NotificationMessage;
import com.example.AutoskolaDemoWithSecurity.repositories.NotificationMessageRepository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class NotificationsScheduler {
    
    @Autowired
    private NotificationMessageRepository nmr;
    
    Logger log = LoggerFactory.getLogger(NotificationsScheduler.class);
    
    @Scheduled(cron = "0 0 0 ? * *")
    @PostConstruct
    private void checkNotifications() {
        List<NotificationMessage> messages = nmr.findAll();
        if(!messages.isEmpty()) {
            messages.forEach((mes) -> {
                Date now = new Timestamp(System.currentTimeMillis());
                Date messageDate = mes.getCreationDate();
                messageDate.setHours(messageDate.getHours()+mes.getHoursToDelete());
                if (now.after(messageDate)) {
                    nmr.delete(mes);
                }
            });
        }
    }
    
}
