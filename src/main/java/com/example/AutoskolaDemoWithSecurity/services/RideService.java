
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
import com.example.AutoskolaDemoWithSecurity.utils.RideDateUtil;
import java.util.ArrayList;
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
    private RideDateUtil dateUtil;
    
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    
    public ResponseEntity addRide(RideDTO rideDTO, int relationID) {
        if(dateUtil.isDateValid(rideDTO.getDate())
                && dateUtil.isTimeValid(rideDTO.getTime())) {
            
            Ride ride = new Ride(rideDTO);
            
            ride.setIsFree(true);
            ride.setDrivingSchool(relationshipRepository
                    .findById(relationID).get().getDrivingSchool());
            
            ride.setInstructor(userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get());
            
            rideRepository.save(ride);
            
        }
        return new ResponseEntity("Bad request", HttpStatus.BAD_REQUEST);
    }
    
    public ResponseEntity removeRide(int rideID, int relationID) {
        User student;
        boolean isfree;
        try {
            Ride ride = rideRepository.findById(rideID).get();
            student = ride.getStudent();
            isfree = ride.isFree();
            rideRepository.delete(ride);
        } catch (Exception e) {
            throw new NoSuchElementException("This ride does not exists");
        }
        if(!isfree) {
            // da sa studentovi vediet ze sa jazda zrusila
        }
        return new ResponseEntity("Ride succesfully removed", HttpStatus.OK);
    }
    
    /*
    *checks if date is valid
    *chesks if there are any rides in that date - if not, return empty list;
    */
    public List<InstructorRides> getFreeRides(HttpServletRequest request, String date) {
        if(dateUtil.isDateValid(date)) {
            
            List<InstructorRides> rides = new ArrayList<>();
            Relationship relationship = relationshipRepository
                    .findById(Integer.parseInt(request.getHeader("Relation"))).get();
            
            List<Ride> freeRides = rideRepository.findAllByDrivingSchoolAndDateAndIsFree(
                                    relationship.getDrivingSchool(), date, true);
            
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
