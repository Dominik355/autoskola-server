
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
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
    public ResponseEntity addRides(RideDTO[] rides, int relationID) {

        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();

        for(int i=0; i <rides.length; i++) {
            if(!rideUtil.isRideDTOFine(rides[i], instructor)) {
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
        return new ResponseEntity("Rides succesfully created", HttpStatus.OK);
        
    }
    
    
    public ResponseEntity removeRide(int rideID) throws ParseException{
        User student;
        String status;
        Ride ride;
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        
        try {
            ride = rideRepository.findByIdAndInstructor(rideID, instructor);
            student = ride.getStudent();
            status = ride.getStatus();
            rideRepository.delete(ride);
        } catch (Exception e) {
            throw new NoSuchElementException("This ride does not exists, or is not yours");
        }
        if(status.equals("PENDING") || !rideUtil.isItBeforeRide(ride, 0)) {
           //jazda sa uz mala zacala - oznaci sa ako NOTFINISHED a prida do completed rides, da sa taktiez ziakovi vediet
       } else if (status.equals("RESERVED")) {
           // da sa vediet studentovi ze sa jaza zrusila - CANCELLED 
       } else {
           //jazda je FREE - len ju vymazat a oznacit ako CANCELLED
       }
        return new ResponseEntity("Ride succesfully removed", HttpStatus.OK);
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
                    List<String> times = new ArrayList<>();
                    for(Ride r : freeRides) {
                        if(r.getInstructor().getFullName().equals(name)) {
                            times.add(r.getTime());
                        }
                    }
                    rides.add(new InstructorRides(name, times.toArray(new String[0]), date));
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
    public ResponseEntity reserveRide(HttpServletRequest request, int rideID) {
        User student = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Ride ride = rideRepository.getOne(rideID);
        ride.setStudent(student);
        ride.setStatus("RESERVED");
        rideRepository.save(ride);
        return new ResponseEntity("You succesfully signed to ride", HttpStatus.OK);
    }
    
    //pre studenta - odhlasenie z jazdy.... iba sa odstrani jeho meno a da sa vediet instruktorovi .. moze to urobit maximalne hodinu pred jazdou napr
    public ResponseEntity cancelRide(HttpServletRequest request, int rideID) throws ParseException {
        Ride ride = rideRepository.getOne(rideID);
        if(rideUtil.isItBeforeRide(ride, 1)) {
            User student = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
            //musi to byt v try-cathc, lebo ak ta jazda nema prideleneho ziadneho studenta, hodi nullpointer
            try{
                if(ride.getStudent().equals(student)) {
                ride.setStudent(null);
                ride.setStatus("FREE");
                rideRepository.save(ride);
                //da sa instruktorovi vediet ze sa odhlasil ziak z jazdy    
                } else {
                    return new ResponseEntity("This ride is not assigned to you", HttpStatus.BAD_REQUEST);
                }
            } catch(NullPointerException e) {
                throw new NullPointerException("This ride is not assigned to you");
            }
            return new ResponseEntity("You succesfully signed off the ride", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("You can not sign off ride, 1 hour limit is over", HttpStatus.OK);
    }
    
    
}
