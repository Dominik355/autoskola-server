
package com.example.AutoskolaDemoWithSecurity.validators.constraint;

import com.example.AutoskolaDemoWithSecurity.validators.validator.PasswordStrengthValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {PasswordStrengthValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordStrengthConstraint {
    
  String message() default "Invalid Password";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
