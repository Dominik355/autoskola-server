
package com.example.AutoskolaDemoWithSecurity.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



@Service
public class JwtUtil implements Serializable {
    
    private static final long serialVersionUID = -2550185165626007488L;
    private static final long JWT_TOKEN_VALIDITY = 3300000L;
    @Value("${jwt.secret}")
    private String secret;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject); 
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration); 
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
      Map<String, Object> claims = new HashMap<>();
      return createToken(claims, userDetails.getUsername());
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
        .signWith(SignatureAlgorithm.HS256, this.secret).compact(); 
    }


    public boolean validateToken(String token, UserDetails userDetails) {
      String username = extractEmail(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
}
