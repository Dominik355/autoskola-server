
package com.example.AutoskolaDemoWithSecurity.models.otherModels;


import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetails implements UserDetails {
    
    private long Id;
    private String name;
    private String surname;
    private String password;
    private String email;
    private boolean active;
    private List<GrantedAuthority> authorities;
    private Timestamp createdOn;

    public MyUserDetails(User user) {
      this.Id = user.getId();
      this.name = user.getName();
      this.surname = user.getSurname();
      this.password = user.getPassword();
      this.email = user.getEmail();
      this.active = user.isActive();
      this.createdOn = user.getCreatedOn();
      this.authorities = Arrays.stream(user.getRoles().split(","))
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public MyUserDetails() {
    
    }

      public long getId() {
          return Id;
      }

      public void setId(long Id) {
          this.Id = Id;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }
      
      public String getSurname() {
          return surname;
      }

      public void setSurname(String surname) {
          this.surname = surname;
      }

    @Override
      public String getPassword() {
          return password;
      }

      public void setPassword(String password) {
          this.password = password;
      }

      public String getEmail() {
          return email;
      }

      public void setEmail(String email) {
          this.email = email;
      }

      public boolean isActive() {
          return active;
      }

      public void setActive(boolean active) {
          this.active = active;
      }

    @Override
      public List<GrantedAuthority> getAuthorities() {
          return this.authorities;
      }

      public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
          this.authorities = (List<GrantedAuthority>) authorities;
      }

      public Timestamp getCreatedOn() {
          return createdOn;
      }

      public void setCreatedOn(Timestamp createdOn) {
          this.createdOn = createdOn;
      }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}