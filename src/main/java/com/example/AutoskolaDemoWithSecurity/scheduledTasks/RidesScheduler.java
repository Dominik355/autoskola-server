
package com.example.AutoskolaDemoWithSecurity.scheduledTasks;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CancelledRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.NotificationMessage;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.VerificationToken;
import com.example.AutoskolaDemoWithSecurity.repositories.CancelledRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.NotificationMessageRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.services.NotificationMessageService;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import com.example.AutoskolaDemoWithSecurity.utils.TimeUtil;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RidesScheduler {

    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private TimeUtil timeUtil;
    
    @Autowired
    private CancelledRideRepository crr;
    
    @Autowired
    private NotificationMessageService messageService;
    
    
    Logger log = LoggerFactory.getLogger(RidesScheduler.class);
    
    //reserved -> pending
    //free -> ak mala uz zacat, zrusi ju, a dasa oznam instruktorovi, ze sa vymazala lebo sa nikto neprihlasil
    @Scheduled(cron = "0 1 * ? * *")
    @PostConstruct
    private void checkRideStatus() throws ParseException {
        List<Ride> reservedRides = rideRepository.findAllByStatusAndDate("RESERVED", timeUtil.getTodayDate());
        if(!reservedRides.isEmpty()) {
            for(Ride ride : reservedRides) {
                if(!rideUtil.isItBeforeRide(ride, 0)) {
                    ride.setStatus("PENDING");
                    rideRepository.save(ride);
                }
            }
        }
        List<Ride> freeRides = rideRepository.findAllByStatus("FREE");
        if(!freeRides.isEmpty()) {
            for(Ride ride : freeRides) {
                if(!rideUtil.isItBeforeRide(ride, 0)) {
                    rideRepository.delete(ride);
                    crr.save(new CancelledRide(ride));
                    messageService.addNotification(
                            ride.getInstructor(), ride.getDrivingSchool()
                            , "Nobody reserved ride at: "+ride.getDate()+" "+ride.getTime()+", so we deleted it");
                }
            }
        }
    }
    
    //pripomenie, ze ma stale 'PENDING' jazdy - raz za den, o 18:00 - vtedy by mali byt vsetky jazdy z dna odjazdene
    @Scheduled(cron = "0 0 18 ? * *")
    @PostConstruct
    private void forgetToComplete() throws ParseException {
        List<Ride> freeRides = rideRepository.findAllByStatus("PENDING");
        if(!freeRides.isEmpty()) {
            for(Ride ride : freeRides) {
                    messageService.addNotification(
                            ride.getInstructor(), ride.getDrivingSchool()
                            , "Ride: "+ride.getDate()+" "+ride.getTime()+", has not been completed yet");
                    rideRepository.save(ride);
            }
        }
    }  
    
}
