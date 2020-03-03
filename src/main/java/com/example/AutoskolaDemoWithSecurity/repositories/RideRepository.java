
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {
    
    List<Ride> findAllByDrivingSchoolAndDateAndIsFree(DrivingSchool drivingschool, String date, boolean isFree);
    
    boolean existsByTimeAndDateAndInstructor(String time, String date, User user);
 
    Ride findByIdAndInstructor(int id, User user);

}
