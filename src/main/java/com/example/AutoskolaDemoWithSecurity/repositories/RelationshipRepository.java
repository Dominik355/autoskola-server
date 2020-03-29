
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer>{
    
    Relationship findByUser (User user);
    
    boolean existsByUserAndId(User user, int id);
    
    Relationship findByUserAndDrivingSchool(User user, DrivingSchool drivingSchool);
    
}
