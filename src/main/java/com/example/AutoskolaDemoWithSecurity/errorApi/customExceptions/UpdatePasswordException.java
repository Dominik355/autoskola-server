
package com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions;

/*
*   Exception thrown if password match, or original password is wrong
*/

public class UpdatePasswordException extends RuntimeException{
    
    public UpdatePasswordException(String errorMessage) {
        super(errorMessage);
    }

    public UpdatePasswordException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
    
}
