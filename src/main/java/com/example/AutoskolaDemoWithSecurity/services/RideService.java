
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
        for(RideDTO r : rides) {
            if(!rideUtil.isRideDTOFine(r, instructor)) {
                throw new WrongDateException("Wrong object parameters, or it already exists, Ride: "+r.toString());
            }
        }
        Arrays.asList(rides).forEach(t -> addRide(t, relationID, instructor));
        return new ResponseEntity("Rides succesfully created", HttpStatus.OK);
        
    }
    
    
    public ResponseEntity removeRide(int rideID, int relationID) throws ParseException{
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
        if(status.equals("PENDING") || rideUtil.hasItStarted(ride)) {
           //jazda sa uz zacala - oznaci sa ako NOTFINISHED
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
                    rides.add(new InstructorRides(name, times.toArray(new String[0])));
                }
                return rides; 
            } else {
                return rides;
            }
        } else {
            throw new WrongDateException("Wrong date");
        }     
    }
    
    /*
    *
    *//*
    public ResponseEntity reserveRide(HttpServletRequest request, RideDTO reservation) {
        
    }
    */
    
}
