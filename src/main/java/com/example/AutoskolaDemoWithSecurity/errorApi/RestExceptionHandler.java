
package com.example.AutoskolaDemoWithSecurity.errorApi;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.CustomLoginException;
import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.UpdatePasswordException;
import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.WrongDateException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.security.auth.login.LoginException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler { 

    Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
    
    
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable
            (HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {           
      String error = "Malformed JSON request";
      return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
      ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
      apiError.setMessage(ex.getMessage());
      return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
                
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Bad request!");
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if(errors.size() > 0) {
            errors.stream().forEach((error) -> {
                
                  String errorMessage = error.getCodes()[0];
                  String [] splitted = errorMessage.split("\\.");
                  if(splitted.length > 2) {
                        String objectName = splitted[1];
                        String field = splitted[2];
                        String message = error.getDefaultMessage();

                        apiError.addSubError(new ApivalidationError(objectName, field, message));   
                  } else {
                        apiError.addSubError(new ApivalidationError(error.getDefaultMessage()));
                  }
            });
           return buildResponseEntity(apiError);
        }
        return buildResponseEntity(apiError);
    }
            
            
    @ExceptionHandler({ ExpiredJwtException.class, UnsupportedJwtException.class,
                MalformedJwtException.class, SignatureException.class, JwtException.class })
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        System.out.println("com.example.AutoskolaDemoWithSecurity.errorApi.RestExceptionHandler.handleExpiredJwtException()");
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }
            
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }
    
    @ExceptionHandler({DataAccessException.class, HibernateException.class})
    public ResponseEntity<Object> handleDatabaseProblemException(RuntimeException ex) {
        ex.printStackTrace();
        String errorMessage = ex.getMessage();
        if(errorMessage == null) {
            errorMessage = ex.getLocalizedMessage();
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage));
    }
    
    @ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }    
    
    @ExceptionHandler({ MailException.class })
    public ResponseEntity<Object> handleMailException(MailException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }    
    
    @ExceptionHandler({ PersistenceException.class })
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex) {
        ex.printStackTrace();
            String errorMessage = ex.getMessage();
        if(errorMessage == null) {
            errorMessage = ex.getLocalizedMessage();
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage));
    }     
    
    @ExceptionHandler({ EntityExistsException.class })
    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }    
    
    @ExceptionHandler({ UpdatePasswordException.class })
    public ResponseEntity<Object> handleUpdatePasswordException(UpdatePasswordException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }   
    
    @ExceptionHandler({ WrongDateException.class })
    public ResponseEntity<Object> handleWrongDateException(WrongDateException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }  
    
    @ExceptionHandler({ LoginException.class })
    public ResponseEntity<Object> handleLoginException(LoginException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }  
    
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    
    @ExceptionHandler({ CustomLoginException.class })
    public ResponseEntity<Object> handleCustomLoginException(CustomLoginException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    
    @ExceptionHandler({ InterruptedException.class })
    public ResponseEntity<Object> handleInterruptedException(InterruptedException ex) {
        String errorMessage = ex.getMessage();
        if(errorMessage == null) {
            errorMessage = "Something went wrong, request has been Interrupted";
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMessage));
    } 
    
    @ExceptionHandler({ NullPointerException.class })
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "NullPointerException: "+ex.getMessage()));
    }
    
    @ExceptionHandler({ IOException.class })
    public ResponseEntity<Object> handleIOException(IOException ex) {
        System.out.println("IOException occured with message: " + ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleNoHandlerFoundException(ex, headers, status, request); 
          return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "This address does not exist!"));
    }
    
    public ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        ResponseEntity re = new ResponseEntity(apiError, apiError.getStatus());
        log.error(apiError.toString());
        return re; 
    }
    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
}