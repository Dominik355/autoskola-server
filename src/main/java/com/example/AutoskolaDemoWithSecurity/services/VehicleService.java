
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Vehicle;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.VehicleDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.VehicleRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
   
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    public String addVehicle(VehicleDTO vehicleDTO, int relationID) {
        Vehicle vehicle = new Vehicle(vehicleDTO);
        vehicle.setOwner(relationshipRepository.findById(relationID).get().getDrivingSchool());
        vehicleRepository.save(vehicle);
        return "Vehicle succesfully saved";
    }
    
    public String removeVehicle(int vehicleID, int relationID) {
        vehicleRepository.deleteByIdAndOwner(vehicleID, relationshipRepository.findById(relationID).get().getDrivingSchool());
        return "Vehicle succesfully removed";
    }
    
    public String modifyVehicle(VehicleDTO vehicleDTO, int relationID) {
        Vehicle vehicle = vehicleRepository.getOne(vehicleDTO.getId());
        if(vehicle.getOwner().equals(
                relationshipRepository.findById(relationID).get().getDrivingSchool())) {
        
            vehicle.setEvidenceNumber(vehicleDTO.getEvidenceNumber());
            vehicle.setName(vehicleDTO.getName());
            vehicle.setType(vehicleDTO.getType());
            vehicleRepository.save(vehicle);
            return "Vehicle succesfully modified";
        } else {
            throw new SecurityException("This vehicle does not belong to your school!");
        }
    }
    
    public List<VehicleDTO> getVehicles(int relationID) {
        return vehicleRepository.findAllByOwner(
                relationshipRepository.findById(relationID).get()
                        .getDrivingSchool()).stream().map(VehicleDTO::new).collect(Collectors.toList());
        
    }
    
    public VehicleDTO getVehicle(int relationID, int vehicleID) {
        return new VehicleDTO(vehicleRepository.findByIdAndOwner(
                vehicleID, relationshipRepository.findById(relationID).get().getDrivingSchool())
                .orElseThrow(() -> new NoSuchElementException("No vehicle with this ID")));
    }
    
}
