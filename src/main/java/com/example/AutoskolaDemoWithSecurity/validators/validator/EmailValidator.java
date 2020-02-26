
package com.example.AutoskolaDemoWithSecurity.validators.validator;


import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<EmailValidConstraint, String> {
    
  private Pattern pattern;
  private Matcher matcher;
  private static final String EMAIL_PATTERN = "^[\\w!#$%&{|}~^-]+(?:\\.[\\w!#$%&{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    this.pattern = Pattern.compile(EMAIL_PATTERN);
    this.matcher = this.pattern.matcher(email);
    return this.matcher.matches();
  }

}
