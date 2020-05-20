
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Ride;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {
    
    List<Ride> findAllByDrivingSchoolAndDateAndStatus(DrivingSchool drivingschool, String date, String status);
            
    boolean existsByTimeAndDateAndInstructorAndDrivingSchool(String time, String date, User user, DrivingSchool drivingSchool);
    
    boolean existsByTimeAndDateAndInstructor(String time, String date, User user);
    
    boolean existsByIdAndDrivingSchoolAndStatus(int id, DrivingSchool drivingSchool, String status);
 
    List<Ride> findAllByInstructorAndDrivingSchool(User instructor, DrivingSchool drivingSchool);
    
    List<Ride> findAllByInstructorAndDrivingSchoolAndDate(User instructor, DrivingSchool drivingSchool, String date);
    
    List<Ride> findAllByStudentAndDrivingSchool(User student, DrivingSchool drivingSchool);
        
    Ride findByIdAndInstructor(int id, User instructor);
    
    Ride findByIdAndInstructorAndDrivingSchool(int id, User instructor, DrivingSchool drivingSchool);
    
    Ride findByTimeAndDateAndInstructorAndDrivingSchool(String time, String date, User instructor, DrivingSchool drivingSchool);
    
    boolean existsByInstructorAndDrivingSchoolAndDate(User instructor, DrivingSchool drivingSchool, String date);
    
    List<Ride> findAllByStudentAndDrivingSchoolAndStatus(User student, DrivingSchool drivingSchool, String status);
    
    List<Ride> findAllByInstructorAndDrivingSchoolAndStatus(User instructor, DrivingSchool drivingSchool, String status);
    
    List<Ride> findAllByStatus(String status);
    
    List<Ride> findAllByStatusAndDate(String status, String date);
        
}
