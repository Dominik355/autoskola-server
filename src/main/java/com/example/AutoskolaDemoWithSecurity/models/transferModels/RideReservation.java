
package com.example.AutoskolaDemoWithSecurity.models.transferModels;

import java.io.Serializable;


public class RideReservation implements Serializable {
    
    private int value;  //time without :
    private String label; //time
    private boolean isChecked; //if it will be created

    public RideReservation() {
    
    }

    public RideReservation(String label, boolean isChecked) {
        this.label = label;
        this.isChecked = isChecked;
        this.value = Integer.parseInt(label.replaceAll(":", ""));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }  
    
}
