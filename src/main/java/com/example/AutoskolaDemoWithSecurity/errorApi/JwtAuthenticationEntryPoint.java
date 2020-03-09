
package com.example.AutoskolaDemoWithSecurity.errorApi;


import com.example.AutoskolaDemoWithSecurity.errorApi.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

//Je volany pri kazdej Exception, ktora extenduje AuthenticationException

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
   /* @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;*/
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("inside entry point");
        ApiError err = new ApiError(HttpStatus.UNAUTHORIZED, "You need to log in!   Exception: "+authException.getMessage());
        ResponseEntity error = new ResponseEntity(err, err.getStatus());
       /* if(!isItWrongUrl(request)) {
            System.out.println("Wrong url...............");
            error = new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "This link does not exist"), HttpStatus.BAD_REQUEST);
        }    */    
        try {
            System.out.println("Sending error from EntryPoint");
            OutputStream out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, error);
            out.flush();
        } catch (IOException ex) {
            System.out.println("Exception occured in entry point, sending error in catch");
            response.sendError(401, "You need to log in!, Exception: "+authException.getMessage());
            throw new IOException("Failed to send Entry Point eerror");
        }
    }
    /*
    private boolean isItWrongUrl(HttpServletRequest request) {
        String requestedURI = "{"+request.getMethod()+" "+request.getRequestURI()+"}";
        Object[] mappedUrls = requestMappingHandlerMapping.getHandlerMethods().keySet().toArray();
        boolean flag = false;
        for(Object s : mappedUrls){
            if(requestedURI.equals(s.toString())){
                flag = true;
            }
        }
       return flag;
    }*/
    
}