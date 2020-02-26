
package com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions;

/*
*   Exception thrown in RideDateUtil.class
*/

public class WrongDateException extends RuntimeException {
    
    public WrongDateException(String errorMessage) {
        super(errorMessage);
    }

    public WrongDateException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
    
}
