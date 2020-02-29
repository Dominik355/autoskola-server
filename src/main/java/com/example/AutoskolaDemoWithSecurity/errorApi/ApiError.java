
package com.example.AutoskolaDemoWithSecurity.errorApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


public class ApiError {

    private HttpStatus status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private String message;
    
    private List<ApiSubError> subErrors;
    
    public ApiError() {}
    
    public ApiError(HttpStatus status) {
      this();
      this.status = status;
      this.message = "Unexpected error";
    }

    public ApiError(HttpStatus status, String message) {
      this();
      this.status = status;
      this.message = message;
    }


    public void addSubError(ApiSubError subError) {
      if (this.subErrors == null) {
        this.subErrors = new ArrayList<>();
      }
      this.subErrors.add(subError);
    }


    public void addValidationError(String object, String field, String message) { 
        addSubError(new ApivalidationError(object, field, message)); 
    }

    public void addValidationError(String object, String message) {
            addSubError(new ApivalidationError(object, message)); 
    }

    public void addValidationError(FieldError fieldError) {
      addValidationError(fieldError
          .getObjectName(), fieldError
          .getField(), fieldError
          .getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError); 
    }


    public void addValidationError(ObjectError objectError) {
      addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
    }



    public void addValidationErrorss(List<ObjectError> objectErrors) { 
            objectErrors.forEach(this::addValidationError); 
    }


    public void addValidationError(ConstraintViolation<?> constraintViolation) {
      addValidationError(constraintViolation
          .getRootBeanClass().getSimpleName(), ((PathImpl)constraintViolation
          .getPropertyPath()).getLeafNode().asString(), constraintViolation
          .getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError); 
    }

    public HttpStatus getStatus() {
        return this.status; 
    }

    public void setStatus(HttpStatus status) {
            this.status = status; 
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp; 
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp; 
    }

    public String getMessage() {
        return this.message; 
    }

    public void setMessage(String message) {
        this.message = message; 
    }

    public List<ApiSubError> getSubErrors() {
        return this.subErrors; 
    }

    public void setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors; 
    }

    @Override
    public String toString() {
        return "Timestamp: "+this.getTimestamp()
                +", Status: "+this.getStatus()
                +", Message: "+this.getMessage();
    }
    
}