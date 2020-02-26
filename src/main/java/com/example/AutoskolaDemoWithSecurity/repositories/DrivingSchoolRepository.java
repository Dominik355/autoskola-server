
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DrivingSchoolRepository extends JpaRepository<DrivingSchool, Integer> {
    
    boolean existsByName(String name);
    
}
