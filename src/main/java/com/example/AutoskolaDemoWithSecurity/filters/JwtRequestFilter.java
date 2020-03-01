
package com.example.AutoskolaDemoWithSecurity.filters;

  
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import com.example.AutoskolaDemoWithSecurity.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
        
        String requestTokenHeader = request.getHeader("Authorization");
        String email = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
          jwtToken = requestTokenHeader.substring(7);
          try {
            email = this.jwtUtil.extractEmail(jwtToken);
          } catch (ExpiredJwtException e) {
              throw e;
          } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
              throw e;
          } catch (Exception e) {
              throw e;
          }
        } else {
          System.out.println("JWT Token does not begin with Bearer String or Authorization header is null");
        } 

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(email);
            if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken token = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // neviem na co su tieto detaily, getName vracia hodnotu z principals, a tam je vlastne ulozeny cely userdetails,
            //takze do  details si mozem dat lubovolny objekt
              token.setDetails((new WebAuthenticationDetailsSource()).buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(token);
            }
            else {
                System.out.println("Token is not valid"); 
            }  
        }
        else {
            System.out.println("username is null "); 
        }

        filterChain.doFilter(request, response);
      }

}
