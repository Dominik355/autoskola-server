
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Entity
@Table(name = "users")
@EnableTransactionManagement
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true, nullable = false)
    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Column(nullable = false)
    @ApiModelProperty(notes = "8 az 30 znakov, 1 male pismeno, 1 velke, 1 cislica, nie specialne znaky")
    private String password;

    @Column(unique = true, nullable = false)
    @NotEmpty
    private String email;

    @NotEmpty
    @Column(name = "phone_number")
    private String phoneNumber;

    private boolean active;

    @NotEmpty
    private String roles;

    @Column(updatable = false, name = "created_on")
    private Timestamp createdOn;


    public User(String name, String surname, String password, String email, String phoneNumber, String roles) {
      this.name = name;
      this.surname = surname;
      this.password = password;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.active = false;
      this.roles = roles;
      this.createdOn = new Timestamp(System.currentTimeMillis());
    }

    public User(UserDTO userDTO) {
      String[] names = userDTO.getFullName().split(" ");
      this.name = names[0];
      this.surname = names[1];
      this.password = userDTO.getPassword();
      this.email = userDTO.getEmail();
      this.phoneNumber = userDTO.getPhoneNumber();
      this.active = false;
      this.roles = userDTO.getRoles();
      this.createdOn = new Timestamp(System.currentTimeMillis());
    }

    public User() {
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getFullName() {
        return this.name+" "+this.surname;
    }

    @Override
    public String toString() {
        return this.getFullName()+", "
                +this.id+", "
                +this.roles+", "
                +this.getEmail()+", "
                +this.phoneNumber;
    }
    
}