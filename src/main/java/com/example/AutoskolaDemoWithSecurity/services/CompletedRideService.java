
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.constants.RelationshipConstants;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.CompletedRideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    
    @Autowired
    private RideUtil rideUtil;
    
    @Autowired
    private RelationshipRepository relationRepository;
    
    @Autowired
    private NotificationMessageService notificationService;
    
    @Autowired
    private MessageSource messageSource;
    
    
    public List<RideDTO> getCompletedRides(int relationID, String date) {
        
        User user = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        DrivingSchool school = relationRepository.findById(relationID).get().getDrivingSchool();
        List<CompletedRide> rides = new ArrayList<>();
        List<Ride> ridess = new ArrayList<>();
        List<RideDTO> response = new ArrayList<>();
        
        if(user.getRoles().contains("STUDENT")) {
            rides = crr.findAllByDrivingSchoolAndStudent(school, user);
            ridess = rideRepository.findAllByStudentAndDrivingSchoolAndStatus(user, school, "PENDING");
        } else if(user.getRoles().contains("INSTRUCTOR")
                || user.getRoles().contains("OWNER")) {
            rides = crr.findAllByDrivingSchoolAndInstructor(school, user);
            ridess = rideRepository.findAllByInstructorAndDrivingSchoolAndStatus(user, school, "PENDING");
        }
        if(!date.equals("")) {
            response.addAll(rides.stream().filter(ride -> date.equals(ride.getDate()))
                    .map(RideDTO::new).collect(Collectors.toList()));
            response.addAll(ridess.stream().filter(ride -> date.equals(ride.getDate()))
                    .map(RideDTO::new).collect(Collectors.toList()));
            return response;
        }
        response.addAll(rides.stream().map(RideDTO::new)
                .collect(Collectors.toList()));
        response.addAll(ridess.stream().map(RideDTO::new)
                .collect(Collectors.toList()));
        return response;
    }
        
    //jazda musi mat oznacenie PENDING a oznaci sa  ako FINISHED, ulozi sa ked tak novy komentar k nej
    public ResponseEntity completeRide(RideDTO rideDTO, int relationID) throws ParseException {
      
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        
        Ride ride = rideRepository.findByIdAndInstructorAndDrivingSchool(rideDTO.getId(), instructor,
                    relationRepository.findById(relationID).get().getDrivingSchool());
        if(ride != null) {
            if(ride.getStatus().equals("PENDING") || !rideUtil.isItBeforeRide(ride, 0)) {
                if(ride.getStudent().isPresent()) {
                        CompletedRide cRide = new CompletedRide(ride);
                        cRide.setComment(rideDTO.getComment().orElse(ride.getComment().orElse("")));

                        cRide.setStatus("FINISHED");
                        rideRepository.delete(ride);
                        CompletedRide compR = crr.save(cRide);
                        checkStatus(ride);
                        if(compR != null) {
                            notificationService.addPushNotification(ride.getStudent().get()
                                            , relationRepository.findById(relationID).get().getDrivingSchool()
                                            ,messageSource.getMessage(
                                                    "instructor.completedRide", new Object[] {ride.getDate(), ride.getTime()}, Locale.ROOT));
                            return new ResponseEntity("Jazda dokončená", HttpStatus.OK);
                        }
                        
                } else {
                    return new ResponseEntity(messageSource.getMessage("ride.noStudent", null, Locale.ROOT), HttpStatus.BAD_REQUEST);
                }
            }
            throw new WrongDateException(messageSource.getMessage("ride.notStarted", null, Locale.ROOT));
        }
        throw new NoSuchElementException(messageSource.getMessage("ride.notExists", null, Locale.ROOT));
    }
    
    
    //vyberie jazdy z poslednych 5 dni a z nich 'count' poslednych
    public List<RideDTO> getLastRides(int count, int relationID) {
        
        User instructor = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<CompletedRide> last = new ArrayList<>();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Timestamp(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // len z poslednych 2 tyzdnov
        for(int i = 0; i < 14; i++) {

            String d = formatter.format(date).substring(0, 10);
            List<CompletedRide> pridavny = crr.findAllByInstructorAndDateAndDrivingSchool(instructor, d,
                        relationRepository.findById(relationID).get().getDrivingSchool());
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
    
    
    private void checkStatus(Ride ride) {
        int size = crr.findAllByStudentAndDrivingSchoolAndStatus(
                ride.getStudent().get(), ride.getDrivingSchool(), "FINISHED").size();
        if(size >= 15) {
            Relationship re = relationRepository.findByUserAndDrivingSchool(ride.getStudent().get(), ride.getDrivingSchool());
            re.setStatus(RelationshipConstants.COMPLETED);
            re.setActivate(false);
            relationRepository.save(re);
            notificationService.addPushNotification(re.getUser(), re.getDrivingSchool()
                    , messageSource.getMessage("student.completedRides", null, Locale.ROOT));
        }
    }
    
}
