
package com.example.AutoskolaDemoWithSecurity.validators.constraint;


import com.example.AutoskolaDemoWithSecurity.validators.validator.EmailValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EmailValidator.class})
public @interface EmailValidConstraint {
    
  String message() default "Invalid email !";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}