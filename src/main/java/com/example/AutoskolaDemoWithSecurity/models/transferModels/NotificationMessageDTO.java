/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.NotificationMessage;
import java.io.Serializable;
import java.sql.Timestamp;


public class NotificationMessageDTO implements Serializable {

    private Timestamp date;
    
    private String message;

    public NotificationMessageDTO() {
    
    }
    
    public NotificationMessageDTO(NotificationMessage message) {
        this.date = message.getCreationDate();
        this.message = message.getMessage();
    }
    
    public NotificationMessageDTO(Timestamp date, String message) {
        this.date = date;
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
