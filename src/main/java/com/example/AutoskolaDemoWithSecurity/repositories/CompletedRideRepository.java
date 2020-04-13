
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.CompletedRide;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.DrivingSchool;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompletedRideRepository extends JpaRepository<CompletedRide, Integer>{
    
    List<CompletedRide> findAllByInstructor(User instructor);
    
    List<CompletedRide> findAllByInstructorAndDateAndStatus(User instructor, String date, String status);
    
    List<CompletedRide> findAllByInstructorAndDateAndDrivingSchool(User instructor, String date, DrivingSchool drivingSchool);
    
    List<CompletedRide> findAllByDrivingSchoolAndStudent(DrivingSchool drivingSchool, User student);

    List<CompletedRide> findAllByDrivingSchoolAndInstructor(DrivingSchool drivingSchool, User instructor);
    
    List<CompletedRide> findAllByStudentAndDrivingSchoolAndStatus(User student, DrivingSchool drivingSchool, String status);
    
    List<CompletedRide> findAllByInstructorAndDrivingSchoolAndStatus(User student, DrivingSchool drivingSchool, String status);
    
}
