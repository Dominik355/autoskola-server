
package com.example.AutoskolaDemoWithSecurity.validators.validator;

import com.example.AutoskolaDemoWithSecurity.repositories.DrivingSchoolRepository;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.UniqueSchoolName;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UniqueSchoolNameValidator implements ConstraintValidator<UniqueSchoolName, String> {

    @Autowired
    DrivingSchoolRepository schoolRepository;
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !(schoolRepository.existsByName(value));
    }
    
}
