
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class RideIdTime implements Serializable{
    
    int id;
    String time;

    public RideIdTime() {
    
    }

    public RideIdTime(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
