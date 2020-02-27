
package com.example.AutoskolaDemoWithSecurity.validators.validator;

import com.example.AutoskolaDemoWithSecurity.utils.RoleUtil;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.RoleConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class RoleValidator implements ConstraintValidator<RoleConstraint, String>{

    @Autowired
    RoleUtil roleUtil;
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return roleUtil.isRole(value);
        
    }
    
}
