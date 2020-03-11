
package com.example.AutoskolaDemoWithSecurity.utils;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.RideDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.RideRepository;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Date format .... [year-month-day]...[2020-08-25]
@Service
public class RideUtil {
    
    @Autowired
    private RideRepository rideRepository;
    
    
    public boolean hasItStarted(Ride ride) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Timestamp(System.currentTimeMillis());
        Date rideDate = formatter.parse(ride.getDate()+" "+ride.getTime());
        return rideDate.before(now);
    }
    
    public boolean isDateValid(String date) {
        
        if(date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            
            String[] params = date.split("-");
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.parseInt(params[0]);
            int month = Integer.parseInt(params[1]);
            int day = Integer.parseInt(params[2]);
            if((day > 0 && day < 32) 
                    && (month > 0 && month < 13) 
                    && (year == currentYear || year == currentYear+1)) {
                return true;
            } else {            
                System.out.println("You can not use this date");
                throw new WrongDateException("You can not use this date");
            }
        } else {
            System.out.println("Bad date format");
            throw new WrongDateException("Bad date format! use: year-month-day, example: 2020-08-25");
        }
        
    }
    
    public boolean isTimeValid(String time) {
        
        if(time.matches("\\d{2}:\\d{2}")) {
            String[] dt = time.split(":");
            
            if(Integer.parseInt(dt[0]) < 25
                    && Integer.parseInt(dt[1]) < 60) {
                return true;
            } else {
                System.out.println("This time does not exists!");
                throw new WrongDateException("This time does not exists!");
            }
            
        } else {
            System.out.println("Wrong time format");
            throw new WrongDateException("Wrong time format! use : hours:minutes, example: 08:30");
        }
        
    }
    
    private enum RideStatus {
        
        FREE("FREE"),
        RESERVED("RESERVED"),
        PENDING("PENDING"),
        FINISHED("FINISHED"),
        CANCELLED("CANCELLED"),
        NOTFINISHED("NOTFINISHED");
        
        public final String status;

        private RideStatus(String name) {
            this.status = name;
        }

        public String getName() {
            return status;
        }    

        @Override
        public String toString() {
            return status;
        }
        
    }
    
    public boolean isItStatus(String status) {
        status = statusCorrection(status);
        for(RideStatus st : RideStatus.values()) {
            if(status.equals(st.toString())) {
                return true;
            }
        }
        throw new NoSuchElementException(
                "Status have to be one of these: "+Arrays.toString(RideStatus.values()));
    }
    
    public String statusCorrection(String status) {
        return status.replaceAll("\\s+", "").toUpperCase();
    }
    
    public boolean isRideDTOFine(RideDTO rideDTO, User instructor) {
        if(isDateValid(rideDTO.getDate())
                && isTimeValid(rideDTO.getTime())) {
            if (!rideRepository.existsByTimeAndDateAndInstructor(
                    rideDTO.getTime(), rideDTO.getDate(), instructor)) {
                return true;
            } else {
                throw new EntityExistsException("This ride already exists!"
                        + " Ride: [Date: "+rideDTO.getDate()+", Time"+rideDTO.getTime()+"]");
            }
        } else {
            throw new WrongDateException("Invalid Date or Time! on ride: "+rideDTO.toString());
        }
    }
    
}
