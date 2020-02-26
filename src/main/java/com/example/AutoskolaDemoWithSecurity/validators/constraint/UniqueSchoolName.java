
package com.example.AutoskolaDemoWithSecurity.validators.constraint;

import com.example.AutoskolaDemoWithSecurity.validators.validator.UniqueSchoolNameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueSchoolNameValidator.class})
public @interface UniqueSchoolName {
    
  String message() default "This school already exists !";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
    
}
