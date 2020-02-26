
package com.example.AutoskolaDemoWithSecurity.validators.validator;


import com.example.AutoskolaDemoWithSecurity.validators.constraint.PasswordStrengthConstraint;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class PasswordStrengthValidator
  implements ConstraintValidator<PasswordStrengthConstraint, String> {

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 30;
    
  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
      
      if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
          return false;
      }
      
      int uppercase = 0;
      int lowercase = 0;
      int digits = 0;
      int specialCharacters = 0;
      char[] passwordChars = password.toCharArray();
      
      for (int index = 0; index < password.length(); index++) {
          if(Character.isUpperCase(passwordChars[index])) {
              uppercase += 1;
          }
          if(Character.isLowerCase(passwordChars[index])) {
              lowercase += 1;
          }
          if(Character.isDigit(passwordChars[index])) {
              digits += 1;
          }
          if(Character.isUpperCase(passwordChars[index])) {
              uppercase += 1;
          }
      }
      if(password.matches("(?=.[!@#\\$%\\^&]) ")) {
          specialCharacters += 1;
      }
      
      return (uppercase >= 1) && (lowercase >= 1) && (digits >= 1) && (specialCharacters == 0);
  }
  
}