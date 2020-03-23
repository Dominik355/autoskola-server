/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AutoskolaDemoWithSecurity.validators.constraint;

import com.example.AutoskolaDemoWithSecurity.validators.validator.RideDateValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RideDateValidator.class})
public @interface RideDateConstraint {
    
  String message() default "Date is incorrect, or already passed";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
