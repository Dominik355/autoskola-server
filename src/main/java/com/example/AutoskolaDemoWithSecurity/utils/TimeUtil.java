/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AutoskolaDemoWithSecurity.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TimeUtil {
    
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    //Date format .... [year-month-day]...[2020-08-25]
    public String getTodayDate() {
        Date now = new Timestamp(System.currentTimeMillis());
        return formatter.format(now);
    }
    
    public Timestamp getDateMinusDays(int minusDays) {
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(referenceDate); 
        c.add(Calendar.DAY_OF_MONTH, -minusDays);
        return new Timestamp(c.getTime().getTime());
    }
    
}
