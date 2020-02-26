
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;


public class AuthenticationRequest implements Serializable {
    
  private static final long serialVersionUID = 5926468583005150707L;
  
  @NotEmpty
  private String email;
  
  @NotEmpty
  private String password;
  
  public AuthenticationRequest() {}
  
  public AuthenticationRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  
  public String getEmail() {
      return this.email; 
  }

  public void setEmail(String email) {
      this.email = email; 
  }

  public String getPassword() {
      return this.password; 
  }

  public void setPassword(String password) {
      this.password = password; 
  }
}
