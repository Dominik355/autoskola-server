
package com.example.AutoskolaDemoWithSecurity.validators.constraint;

import com.example.AutoskolaDemoWithSecurity.validators.validator.RoleValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RoleValidator.class})
public @interface RoleConstraint {
    
  String message() default "Wrong role";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
