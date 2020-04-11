
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ConfirmUserVerification;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmUserVerification, Integer> {
    
    List<ConfirmUserVerification> findAllByDrivingSchool(DrivingSchool drivingSchool);
    
    void deleteByRelation(int relation);
    
    Optional<ConfirmUserVerification> findByRelation(int relation);
    
}
