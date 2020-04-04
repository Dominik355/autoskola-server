
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class ProfilePicture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false, unique = true)
    private int id;

    @Column(name = "user_email")
    @NotNull
    private String name;
  
    private String type;
    
    @NotEmpty
    private byte[] picture;
    
    public ProfilePicture() {
    
    }

    public ProfilePicture( String name, String type, byte[] picture) {
        this.name = name;
        this.type = type;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

}
