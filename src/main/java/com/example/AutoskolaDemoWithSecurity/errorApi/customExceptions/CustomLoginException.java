
package com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions;

//Exception thrown if login has bad credentials
public class CustomLoginException extends RuntimeException {

    public CustomLoginException(String message) {
        super(message);
    }

    public CustomLoginException(Throwable t, String message) {
        super(message, t);
    }
    
}
