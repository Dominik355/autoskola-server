
package com.example.AutoskolaDemoWithSecurity.validators.constraint;


import com.example.AutoskolaDemoWithSecurity.validators.validator.PasswordMatchesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordMatchesValidator.class})
public @interface PasswordMatchesConstraint {
    
  String message() default "Passwords don't match";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}

