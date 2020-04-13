
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer>{
    
    Relationship findByUser (User user);
    
    boolean existsByUserAndId(User user, int id);
    
    Relationship findByUserAndDrivingSchool(User user, DrivingSchool drivingSchool);
    
    Optional<Relationship> findByUserAndId(User user, int id);
    
    List<Relationship> findAllByUser(User user);
    
    List<Relationship> findAllByDrivingSchoolAndStatus(DrivingSchool drivingSchool, String status);
    
}
