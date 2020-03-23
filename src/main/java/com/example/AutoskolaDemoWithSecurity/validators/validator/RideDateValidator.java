
package com.example.AutoskolaDemoWithSecurity.validators.validator;

import com.example.AutoskolaDemoWithSecurity.utils.RideUtil;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.RideDateConstraint;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class RideDateValidator implements ConstraintValidator<RideDateConstraint, String>{

    @Autowired
    private RideUtil rideUtil;
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(rideUtil.isDateValid(value)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Timestamp(System.currentTimeMillis());
            Date date;
            try {
                date = formatter.parse(value);
                if(date.after(now)) {
                    return true;
                }
            } catch (ParseException ex) {
                Logger.getLogger(RideDateValidator.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
  
}
