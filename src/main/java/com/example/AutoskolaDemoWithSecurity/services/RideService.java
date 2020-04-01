
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CancelledRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideIdTime;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideReservation;
import com.example.AutoskolaDemoWithSecurity.repositories.CancelledRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CompletedRideRepository crr;
    
    @Autowired
    private CancelledRideRepository clrr;
    
    @Autowired
    private NotificationMessageService notificationService;
    
    private String[] rideTimes = new String[]{"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00"};
    
    
    public ResponseEntity addRide(RideDTO rideDTO, int relationID, User instructor) {
            
        Ride ride = new Ride(rideDTO);
        ride.setStatus("FREE");
        ride.setDrivingSchool(relationshipRepository
                .findById(relationID).get().getDrivingSchool());
        ride.setInstructor(instructor);
        rideRepository.save(ride);
        return new ResponseEntity("Ride succesfully created", HttpStatus.OK);
        
    }
    
    // ak je  jazda zla, tak nevytvori ziadnu - jednoduchsie na odosielanie eroru,nez niekolko vytvorit a ostatne nie
    // musi byt spravny datum a cas + musi este len nastat, ta jazda uz nemoze existovat
    public ResponseEntity addRides(RideDTO[] rides, int relationID) throws ParseException {

        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();

        for(int i=0; i <rides.length; i++) {
            if(!rideUtil.isRideDTOFine(rides[i], instructor) 
                    && rideUtil.isItBeforeRide(new Ride(rides[i]), 0)) {
                throw new WrongDateException("Wrong object parameters, or it already exists, Ride: "+rides[i].toString());
            }
            //hlada sa duplikat
            for(int j=i+1; j <rides.length; j++) {
                if(rides[i].getTime().equals(rides[j].getTime())) {
                    throw new WrongDateException("duplicate dates: "+rides[i].getDate());
                }
            }
        }
        Arrays.asList(rides).forEach(t -> addRide(t, relationID, instructor));
        //notificationService.addPushNotification(instructor, relationshipRepository.findById(relationID).get().getDrivingSchool(),
        //                                                            "Instruktor vytvoril jazdy na den: "+rides[0].getDate());
        return new ResponseEntity("Rides succesfully created", HttpStatus.OK);
        
    }
    

    /*
    *checks if date is valid
    *chesks if there are any rides in that date - if not, return empty list;
    */
    public List<InstructorRides> getFreeRides(HttpServletRequest request, String date) {
        if(rideUtil.isDateValid(date)) {
            
            List<InstructorRides> rides = new ArrayList<>();
            Relationship relationship = relationshipRepository
                    .findById(Integer.parseInt(request.getHeader("Relation"))).get();
            
            List<Ride> freeRides = rideRepository.findAllByDrivingSchoolAndDateAndStatus(
                                    relationship.getDrivingSchool(), date, "FREE");
            
            if(!freeRides.isEmpty()){
                
                HashSet<String> set = new HashSet<>();
                freeRides.forEach((r) -> {
                    set.add(r.getInstructor().getFullName());
                }); 
                
                for(String name : set) {
                    List<RideIdTime> times = new ArrayList<>();
                    for(Ride r : freeRides) {
                        if(r.getInstructor().getFullName().equals(name)) {
                            times.add(new RideIdTime(r.getId(), r.getTime()));
                        }
                    }
                    rides.add(new InstructorRides(name, times, date));
                }
                return rides; 
            } else {
                return rides;
            }
        } else {
            throw new WrongDateException("Wrong date");
        }     
    }    

    //pre studenta prihlasenie sa na jazdu.....iba sa prida jeho meno a da sa vediet instruktorovi
    public ResponseEntity reserveRide(int relationID, int rideID) {
        User student = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Ride ride = rideRepository.getOne(rideID);
        if(!ride.getDrivingSchool().equals(
                relationshipRepository.getOne(relationID).getDrivingSchool())) {
            throw new SecurityException("This ride does not belong to your school!");
        }
        ride.setStudent(student);
        ride.setStatus("RESERVED");
        rideRepository.save(ride);
        return new ResponseEntity("You succesfully signed to ride", HttpStatus.OK);
    }
    
    //pre studenta - odhlasenie z jazdy.... iba sa odstrani jeho meno a da sa vediet instruktorovi .. moze to urobit maximalne hodinu pred jazdou napr
    public ResponseEntity cancelRide(int relationID, int rideID) throws ParseException {
        Ride ride = rideRepository.getOne(rideID);
        if(!ride.getDrivingSchool().equals(
                relationshipRepository.getOne(relationID).getDrivingSchool())) {
            throw new SecurityException("This ride does not belong to your school!");
        }
        if(rideUtil.isItBeforeRide(ride, 1)) {
            User student = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
            //musi to byt v try-cathc, lebo ak ta jazda nema prideleneho ziadneho studenta, hodi nullpointer
            try{
                if(ride.getStudent().get().equals(student)) {
                ride.setStudent(null);
                ride.setStatus("FREE");
                rideRepository.save(ride);
                } else {
                    return new ResponseEntity("this ride is not assigned to you", HttpStatus.BAD_REQUEST);
                }
            } catch(NullPointerException e) {
                return new ResponseEntity("This ride is not assigned to you", HttpStatus.BAD_REQUEST);
            }
            notificationService.addPushNotification(ride.getInstructor() //da sa instruktorovi vediet ze sa odhlasil ziak z jazdy    
                                        , ride.getDrivingSchool()
                                        , "Student "+student.getFullName()+" cancelled ride"+ride.getDate()+" "+ride.getTime());
            return new ResponseEntity("You succesfully signed off the ride", HttpStatus.OK);
        }
        return new ResponseEntity("You can not sign off ride, 1 hour limit is over", HttpStatus.BAD_REQUEST);
    }
    
    
    public ResponseEntity getMyRides(int relationID, String date) {
        User user = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        DrivingSchool school = relationshipRepository.findById(relationID).get().getDrivingSchool();
        List<Ride> rides = new ArrayList<>();
        
        if(user.getRoles().contains("STUDENT")) {
            rides = rideRepository.findAllByStudentAndDrivingSchool(user, school);
        } else if (user.getRoles().contains("INSTRUCTOR")
                || user.getRoles().contains("OWNER")) {           
            rides = rideRepository.findAllByInstructorAndDrivingSchool(user, school);
        }
        if(!date.equals("")) {

            return new ResponseEntity(rides.stream().filter(
                        ride -> date.equals(ride.getDate())).map(RideDTO::new)
                            .collect(Collectors.toList()), HttpStatus.OK);
        }
        return new ResponseEntity(rides.stream().map(RideDTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    
    public ResponseEntity showTimes(String date, int relationID) {
        User instructor = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        DrivingSchool school = relationshipRepository
                .findById(relationID).get().getDrivingSchool();
        List<RideReservation> rides = new ArrayList<>();
        
        if(rideRepository.existsByInstructorAndDrivingSchoolAndDate(instructor, school, date)) {
            for(String time : rideTimes) {
                if(rideRepository.existsByTimeAndDateAndInstructorAndDrivingSchool
                                                (time, date, instructor, school)) {
                    rides.add(new RideReservation(time, true));
                } else {
                    rides.add(new RideReservation(time, false));
                }
            }
        } else {
            for(String time : rideTimes) {
                rides.add(new RideReservation(time, true));
            }
        }
        return new ResponseEntity(rides, HttpStatus.OK);
    }
    
}
