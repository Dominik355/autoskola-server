
package com.example.AutoskolaDemoWithSecurity.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class LogFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(LogFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.trace("Address: " + request.getRemoteAddr() +
                           ", Port: " + request.getRemotePort() +
                           ", Host: " +request.getRemoteHost() +
                           ", User: " + request.getRemoteUser() +
                           ", URI: " + request.getRequestURI() +
                           ", method: " + request.getMethod());

        
        filterChain.doFilter(request, response);
    }
    
}
