
package com.example.AutoskolaDemoWithSecurity.models.otherModels;

import java.util.ArrayList;
import java.util.List;


public enum VehicleTypes {
 
    auto("Auto"),
    motorka("Motorka"),
    traktor("Traktor"),
    autobus("Autobus"),
    dodavka("Dodávka"),
    moped("Moped"),
    kamion("Kamión");
 
    public final String text;

    private VehicleTypes(String name) {
        this.text = name;
    }

    public String getName() {
        return text;
    }    

    @Override
    public String toString() {
        return text;
    }
    
    public static VehicleTypes getVehicle(String name) {
        for(VehicleTypes vh : VehicleTypes.values()) {
            if(vh.name().equalsIgnoreCase(name)){
                return vh;
            }
        }
        return null; //throw new illegalargumentexception("no constant with text ... found"
    }
    
    public static List<String> getAvailableVehicles() {
        List<String> vehicles = new ArrayList<>();
        for(VehicleTypes vh : VehicleTypes.values()) {
            vehicles.add(vh.name());
        }
        return vehicles;
    }
    
}
