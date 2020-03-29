
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.NotificationMessage;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.Relationship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Integer> {
    
    List<NotificationMessage> findAllByRelation(Relationship relation);
    
}
