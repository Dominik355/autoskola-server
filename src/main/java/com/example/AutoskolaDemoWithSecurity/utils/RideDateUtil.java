
package com.example.AutoskolaDemoWithSecurity.utils;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import java.util.Calendar;
import org.springframework.stereotype.Service;

//Date format .... [day-month-yeaar]...[25-08-2020]
@Service
public class RideDateUtil {
    
    public boolean isDateValid(String date) {
        
        if(date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            
            String[] params = date.split("-");
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int day = Integer.parseInt(params[0]);
            int month = Integer.parseInt(params[1]);
            int year = Integer.parseInt(params[2]);
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
            throw new WrongDateException("Bad date format");
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
            throw new WrongDateException("Wrong time format");
        }
        
    }
    
}
