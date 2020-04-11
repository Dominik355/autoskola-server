
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.QuestionPhoto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhotoRepository extends JpaRepository<QuestionPhoto, Integer> {
        
    Optional<QuestionPhoto> findByQuestion(int question);
    
}
