
package com.example.AutoskolaDemoWithSecurity.errorApi;

import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

//Je volany vzdy, ked sa vyhodi Vynimka typu AccesDeniedException, alebo jej child

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            System.out.println(auth.getName()+" attempted to access the protected URL: "+request.getRequestURI());
            throw new AccessDeniedException("You have no permission to access : "+request.getRequestURI());
        } 
        else {
            throw new AuthenticationException("You need to log in first!");
        }
    }
    
}
