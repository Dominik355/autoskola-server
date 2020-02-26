
package com.example.AutoskolaDemoWithSecurity.validators.constraint;


import com.example.AutoskolaDemoWithSecurity.validators.validator.CorrectNameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CorrectNameValidator.class})
public @interface CorrectName {
    
  String message() default "Wrong name !";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}