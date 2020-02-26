
package com.example.AutoskolaDemoWithSecurity.validators.validator;

import com.example.AutoskolaDemoWithSecurity.validators.constraint.PhoneNumberConstraint;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PhoneNumberValidator
  implements ConstraintValidator<PhoneNumberConstraint, String> {
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {  
      return (value != null && value.matches("[0-9]+") && value.length() == 10 && value.startsWith("0"));    
  }

}
