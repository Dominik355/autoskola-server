
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer>{
    
    Relationship findByUser (User user);
    
    boolean existsByUserAndId(User user, int id);
    
    Relationship findByUserAndDrivingSchool(User user, DrivingSchool drivingSchool);
    
    Optional<Relationship> findByUserAndId(User user, int id);
    
    List<Relationship> findAllByUser(User user);
    
    List<Relationship> findAllByDrivingSchoolAndRoleOrRole(DrivingSchool drivingSchool, String role, String role2);
    
    List<Relationship> findAllByDrivingSchoolAndStatus(DrivingSchool drivingSchool, String status);
    
    @Query("select a from Relationship a where  a.drivingSchool = :drivingSchool and a.activate = :activate and a.lastOnline <= :lastOnline")
    List<Relationship> findAllByDrivingSchoolAndActivateAndLastOnlineBefore(
            @Param("drivingSchool") DrivingSchool drivingSchool,
            @Param("activate") boolean activate,
            @Param("lastOnline") Timestamp lastOnline);
    
}
