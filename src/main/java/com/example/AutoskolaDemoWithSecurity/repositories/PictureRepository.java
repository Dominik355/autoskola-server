
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ProfilePicture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<ProfilePicture, Integer> {
    
    boolean existsByName(String name);
    
    Optional<ProfilePicture> findByName(String name);
    
}
