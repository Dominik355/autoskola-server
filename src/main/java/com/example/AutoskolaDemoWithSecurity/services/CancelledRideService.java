
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CancelledRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.CancelledRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CancelledRideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private CancelledRideRepository clrr;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
        
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private CompletedRideRepository crr;
    
    @Autowired
    private RelationshipRepository relationRepository;
    
    @Autowired
    private NotificationMessageService notificationService;
    
    
    public ResponseEntity removeRides(RideDTO[] rides, int relationID) throws ParseException{
        User instructor = userRepository.findByEmail(
                     SecurityContextHolder.getContext().getAuthentication().getName()).get();
        DrivingSchool drivingSchool = relationshipRepository.findById(relationID).get().getDrivingSchool();
        
        try{
            for(RideDTO ride : rides) {
                if(!rideRepository.existsByTimeAndDateAndInstructorAndDrivingSchool(ride.getTime(), ride.getDate(), instructor, drivingSchool)) {
                    throw new NoSuchElementException("This ride does not exist, Time:"+ride.getTime()+", date: "+ride.getDate());
                }
            }
        } catch(NullPointerException e) {
            throw new NullPointerException("any Time or Date missing");
        }
        for(int i = 0; i < rides.length; i++) {
            ResponseEntity res = removeRide(rideRepository.findByTimeAndDateAndInstructorAndDrivingSchool(
                    rides[i].getTime(), rides[i].getDate(), instructor, drivingSchool)
                    , relationID
                    , instructor
                    , drivingSchool);
        }
        return new ResponseEntity("Rides succesfully removed", HttpStatus.OK);
    }
    
    
    public ResponseEntity removeRide(Ride passedRide, int relationID, User instructor, DrivingSchool drivingSchool) throws ParseException {
         String status;
         Ride ride = passedRide;
         try {
             status = ride.getStatus();
         } catch (Exception e) {
             e.printStackTrace();
             throw new NoSuchElementException("This ride does not exists");
         }  
         if(status.equals("PENDING") || !rideUtil.isItBeforeRide(ride, 0)) {
             if(!ride.getStudent().isPresent()) {
                 // ak jazda uz zacala,ale nikto sa na nu neprihlasil
                 CancelledRide cRide = new CancelledRide(ride);
                 clrr.save(cRide);
             } else {
                 // jazda zacala a aj sa niekto prihlasil
                CompletedRide cRide = new CompletedRide(ride);
                cRide.setStatus("NOTFINISHED");
                crr.save(cRide);
                notificationService.addPushNotification(ride.getStudent().get(), drivingSchool
                                    , "Ride has not been finished: "+ride.getDate()+" "+ride.getTime());
                //da sa taktiez ziakovi vediet, ale s inou informaciou
             }
         } else  {
             //jazda este nezacala 
             CancelledRide cRide = new CancelledRide(ride);
             clrr.save(cRide);
             if(status.equals("RESERVED") ) {
                notificationService.addPushNotification(ride.getStudent().get(), drivingSchool
                                    , "Instructor cancelled ride: "+ride.getDate()+" "+ride.getTime());
             }
         }
         
         rideRepository.delete(ride);
         return new ResponseEntity("Ride succesfully removed", HttpStatus.OK);
     }
    
}
