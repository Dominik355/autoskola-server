
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;


@Entity
public class NotificationMessage {
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false, insertable = false, unique = true)
    private int id;
    
    @ManyToOne(targetEntity = Relationship.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_ID")
    private Relationship relation;
    
    @Column(updatable = false, name = "created_on")
    private Timestamp creationDate;
    
    @Column(name = "hours_to_delete")
    private int hoursToDelete;
    
    @NotEmpty
    private String message;

    public NotificationMessage() {
    
    }

    public NotificationMessage(Relationship relation, String message) {
        this.relation = relation;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.message = message;
        this.hoursToDelete = 48;
    }
    
    public NotificationMessage(Relationship relation, int hoursToDelete, String message) {
        this.relation = relation;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.hoursToDelete = hoursToDelete;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Relationship getRelation() {
        return relation;
    }

    public void setRelation(Relationship relation) {
        this.relation = relation;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public int getHoursToDelete() {
        return hoursToDelete;
    }

    public void setHoursToDelete(int hoursToDelete) {
        this.hoursToDelete = hoursToDelete;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
