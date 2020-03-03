
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompletedRideRepository extends JpaRepository<CompletedRide, Integer>{
    
    List<CompletedRide> findAllByInstructor(User instructor);
    
    List<CompletedRide> findAllByInstructorAndDate(User instructor, String date);

}
