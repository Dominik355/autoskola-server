
package com.example.AutoskolaDemoWithSecurity.validators.validator;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.CorrectName;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CorrectNameValidator implements ConstraintValidator<CorrectName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!value.startsWith(" ") && !value.matches(".*\\d.*")) {
            String[] d = value.split(" ");
            if(d.length == 2){
                return true;
            }
        }
        return false;
    }
    
}
