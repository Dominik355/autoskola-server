
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.InstructorRides;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RideDateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
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
    
    
    public ResponseEntity addRide(RideDTO rideDTO, int relationID, User instructor) {
            
        Ride ride = new Ride(rideDTO);
        ride.setIsFree(true);
        ride.setDrivingSchool(relationshipRepository
                .findById(relationID).get().getDrivingSchool());
        ride.setInstructor(instructor);
        rideRepository.save(ride);
        return new ResponseEntity("Ride succesfully created", HttpStatus.OK);
            
    }
    
    // ak je  jazda zla, tak nevytvori ziadnu - jednoduchsie na odosielanie eroru
    public ResponseEntity addRides(RideDTO[] rides, int relationID) {

        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        for(RideDTO r : rides) {
            if(!isRideDTOFine(r, instructor)) {
                throw new WrongDateException("1 or more of Objects are bad");
            }
        }
        Arrays.asList(rides).forEach(t -> addRide(t, relationID, instructor));
        return new ResponseEntity("Rides succesfully created", HttpStatus.OK);
        
    }
    
    // vytvara jazdy po 1, ak je nejaka zla , zapise ju - zle sa vytvara error
    // napr. polka sa vytvori, polka nie - odoslu sa len erory
   /* public ResponseEntity addRides(RideDTO[] rides, int relationID) {
        List<RideDTO> list = Arrays.asList(rides);
        List<ResponseEntity> responses = new ArrayList<>();
        list.forEach(t -> responses.add(addRide(t, relationID)));
        List<String> errors = new ArrayList<>();
        
        responses.forEach((t) -> {
            if(t.getStatusCode() == HttpStatus.OK) {
                responses.remove(t);
            } else {
                errors.add((String) t.getBody());
            }
        });
        if(!responses.isEmpty()) {
            return new ResponseEntity(responses, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("All Rides succesfully created", HttpStatus.OK);
        }
    }*/
    
    
    public ResponseEntity removeRide(int rideID, int relationID) {
        User student;
        boolean isfree;
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        
        try {
            Ride ride = rideRepository.findByIdAndInstructor(rideID, instructor);
            student = ride.getStudent();
            isfree = ride.isFree();
            rideRepository.delete(ride);
        } catch (Exception e) {
            throw new NoSuchElementException("This ride does not exists, or is not yours");
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
    
    public boolean isRideDTOFine(RideDTO rideDTO, User instructor) {
        if(dateUtil.isDateValid(rideDTO.getDate())
                && dateUtil.isTimeValid(rideDTO.getTime())) {
            if (!rideRepository.existsByTimeAndDateAndInstructor(
                    rideDTO.getTime(), rideDTO.getDate(), instructor)) {
                return true;
            } else {
                throw new EntityExistsException("This ride already exists!");
            }
        } else {
            throw new WrongDateException("Invalid Date or Time!");
        }
    }
    
}
