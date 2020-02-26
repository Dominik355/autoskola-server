
package com.example.AutoskolaDemoWithSecurity.validators.validator;


import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserDTO;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PasswordMatchesConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatchesConstraint, Object> {
    
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    UserDTO user = (UserDTO)object;
    return user.getPassword().equals(user.getMatchingPassword());
  }
  
}
