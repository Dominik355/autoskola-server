
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CompletedRideService {
    
    @Autowired
    private CompletedRideRepository crr;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RideRepository rideRepository;
    
    
    public List<RideDTO> getCompletedRides(String date) {
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<CompletedRide> rides = crr.findAllByInstructorAndDate(instructor, date);
        return rides.stream().map(RideDTO::new).collect(Collectors.toList());
    }
        
    
    public ResponseEntity completeRide(RideDTO rideDTO) {
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Ride ride = rideRepository.findByIdAndInstructor(rideDTO.getId(), instructor);
        
        if(ride != null) {
            
            CompletedRide cRide = new CompletedRide(ride);
            if(ride.getComment() != null) {
                cRide.setComment(rideDTO.getComment());
            } else {
                cRide.setComment(ride.getComment());
            }
            rideRepository.delete(ride);
            cRide.setStatus("FINISHED");
            CompletedRide compR = crr.save(cRide);
            if(compR != null) {
                return new ResponseEntity("Ride set as completed", HttpStatus.OK);
            }
            
        }
        throw new NoSuchElementException("This ride does not exists!");
    }
    
    
    //vyberie jazdy z poslednych 5 dni a z nich 'count' poslednych
    public List<RideDTO> getLastRides(int count) {
        
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<CompletedRide> last = new ArrayList<>();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Timestamp(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // len z posledneho tyzdna
        for(int i = 0; i < 7; i++) {

            String d = formatter.format(date).substring(0, 10);
            List<CompletedRide> pridavny = crr.findAllByInstructorAndDate(instructor, d);
            last.addAll(pridavny);
            
            c.add(Calendar.DATE, -1);
            date = c.getTime();
        }
        
        Collections.sort(last, (CompletedRide o1, CompletedRide o2) -> {

                try {
                    Date d1 = formatter.parse(o1.getDate()+" "+o1.getTime());
                    Date d2 = formatter.parse(o2.getDate()+" "+o2.getTime());
                    return d2.compareTo(d1);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }   
                return 0;
        });
        // radsej zopakujem ten isty riadok kodu, lebo ak je ten pocet jazd napr.40 - zbytocne upravujem 40 objektov
        if(last.size() > count) {
            return last.subList(0, count).stream().map(RideDTO::new).collect(Collectors.toList());
        } else {
            return last.stream().map(RideDTO::new).collect(Collectors.toList());
        }
    }
    
}
