
package com.example.AutoskolaDemoWithSecurity.models.transferModels;


import com.example.AutoskolaDemoWithSecurity.validators.constraint.CorrectName;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.EmailValidConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PasswordMatchesConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PasswordStrengthConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.PhoneNumberConstraint;
import com.example.AutoskolaDemoWithSecurity.validators.constraint.RoleConstraint;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;


@PasswordMatchesConstraint(message = "{userDTO.passwordMatches}")
public class UserDTO {
    
    @NotEmpty(message = "{userDTO.name.notEmpty}")
    @CorrectName(message = "{userDTO.correctName}")
    private String fullName;

    @NotEmpty
    @PasswordStrengthConstraint(message = "{userDTO.passwordStrength}")
    private String password;

    @NotEmpty
    private String matchingPassword;

    @NotEmpty
    @EmailValidConstraint(message = "{userDTO.emailValid}")
    private String email;

    @NotEmpty
    @PhoneNumberConstraint(message = "{userDTO.validPhoneNumber}")
    @ApiModelProperty(notes = "format 09xx xxx xxx")
    private String phoneNumber;

    @NotEmpty
    @RoleConstraint
    @ApiModelProperty(notes = "3 moznosti : 'STUDENT', 'INSTRUCTOR', 'OWNER'")
    private String roles;

    public UserDTO(String fullName, String password, String email, String phoneNumber, String roles) {
      this.fullName = fullName;
      this.password = password;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.roles = roles;
    }

    public UserDTO() {
     
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

}