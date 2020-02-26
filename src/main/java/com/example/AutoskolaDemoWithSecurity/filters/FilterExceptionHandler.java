
package com.example.AutoskolaDemoWithSecurity.filters;

import com.example.AutoskolaDemoWithSecurity.errorApi.RestExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class FilterExceptionHandler extends OncePerRequestFilter {

    @Autowired
    private RestExceptionHandler handler;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            System.out.println("Exception chytena vo Filtri");
            handleFilterException(response, request, e, HttpStatus.UNAUTHORIZED);
        }
    }
    
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    
    public void handleFilterException(HttpServletResponse response, HttpServletRequest request,
                RuntimeException ex, HttpStatus status) throws JsonProcessingException {
        ResponseEntity entity;
        if(ex instanceof JwtException){
            entity = handler.handleJwtException((JwtException) ex);
        }
        else entity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(convertObjectToJson(entity));
            writer.flush();
        } catch (IOException e) {
            System.out.println("Problem with sendind exception in FilterExceptionHandler");
        }
    }
    
}
