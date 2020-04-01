
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.NotificationMessage;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.NotificationMessageDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.PushNotification;
import com.example.AutoskolaDemoWithSecurity.repositories.NotificationMessageRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class NotificationMessageService {
    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private NotificationMessageRepository notificationRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    private final String NOTIFICATION_URL = "http://notification-service";

    private final Logger log = LoggerFactory.getLogger(NotificationMessageService.class);
    
    @Async
    public void addPushNotification(User user, DrivingSchool drivingSchool,String message) {
        sendPushNotification(new PushNotification(
                new Timestamp(System.currentTimeMillis()),
                user.getEmail(),
                message,
                drivingSchool.getName()
        ));
        
        addNotification(user, drivingSchool, message);
    }
    
    public void addNotification(User user, DrivingSchool drivingSchool,String message) {
        addNotification(
                relationshipRepository.findByUserAndDrivingSchool(user, drivingSchool), message);
    }
    
    public void addNotification(Relationship relation, String message) {
        notificationRepository.save(new NotificationMessage(relation, message));      
    }
    
    
    public List<NotificationMessageDTO> getNotifications(int relationID) {
        //vrati vsetky notifikacie zoradene podla datumu od najnovsej - pre day relatonship
        ArrayList<NotificationMessageDTO> messages = new ArrayList<>(
                notificationRepository
                .findAllByRelation(relationshipRepository.findById(relationID).get())
                .stream()
                .map(NotificationMessageDTO::new)
                .collect(Collectors.toList())
        );
        messages.sort((o1, o2) -> {
            return o2.getDate().compareTo(o1.getDate());
        });
        return messages;
    }
    
    
    public void sendPushNotification(PushNotification notification){
        /*      POMOCOU REST TEMPLATE - NA SYNCHRONNE
        HttpEntity<NotificationObject> entity = new HttpEntity<NotificationObject>(notification);
        ResponseEntity<String> response = restTemplate.postForEntity(
                                NOTIFICATION_URL, entity, String.class);
  
        log.info("Response from notification service: "+response.getStatusCodeValue()+", "+response.getBody());
        */
        try{
            ResponseEntity response = webClientBuilder
                .build()
                .post()
                .uri(NOTIFICATION_URL+"/postMessage")
                .bodyValue(notification)
                .retrieve()
                .toEntity(String.class)
                .block();
        log.info("Response from notification service: "+response.getStatusCodeValue()+", "+response.getBody());
        } catch(Exception e) {
            log.info("Notifikačná služba nie je dostupná");
        }
        
    }


    public String getEmitterID(String email) {
        try{
            ResponseEntity response = webClientBuilder
                    .build()
                    .get()
                    .uri(NOTIFICATION_URL+"/getEmitterID/"+email)
                    .retrieve()
                    .toEntity(String.class)
                    .block(Duration.ofSeconds(4));
            if(!response.equals(null)&& response.getStatusCodeValue()==200) {
                return response.getBody().toString();
            }
        } catch(Exception e) {
            log.info("Notifikačná služba nie je dostupná");
        }
        return "";
    }
    
    
    public String logOutEmitter(String email) {
        try{
            ResponseEntity response = webClientBuilder
                    .build()
                    .get()
                    .uri(NOTIFICATION_URL+"/logOut/"+email)
                    .retrieve()
                    .toEntity(String.class)
                    .block(Duration.ofSeconds(5));
            if(!response.equals(null)&& response.getStatusCodeValue()==200) {
                return response.getBody().toString();
            }
        }catch(Exception e) {
            log.info("Notifikačná služba nie je dostupná");
        }
        return "";
    }
    
    
    public String getPushServerURL() {
        try{
            return discoveryClient.getInstances("notification-service").get(0).getUri().toString();
        } catch(Exception e) {
            log.info("Notifikačná služba nie je dostupná");
            return "";
        }
    }
    
}
